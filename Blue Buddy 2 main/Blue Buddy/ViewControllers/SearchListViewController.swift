//
//  SearchListViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/9/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class SearchListViewController: UIViewController , UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate{

    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var tblViewSearch: UITableView!
    @IBOutlet weak var txtSearch: UITextField!
    @IBOutlet weak var imgSearch: UIImageView!
    var shownIndexes : [IndexPath] = []
    var timer1 = Timer()
    var arrSearchList = NSMutableArray()
    var tap = UITapGestureRecognizer()
    override func viewDidLoad() {
        super.viewDidLoad()
        imgSearch.layer.cornerRadius = 8.0
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true
        tblViewSearch.isHidden = true
        
        self.callSearchWebservice()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func myviewTapped(_ sender: UITapGestureRecognizer) {
        self.view.endEditing(true)
        self.view.removeGestureRecognizer(tap)
    }
    // MARK: -
    // MARK: - UitableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrSearchList.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "searchList_Cell", for: indexPath) as! SearchListTableViewCell
        let objList : ProfileBO = arrSearchList[indexPath.row] as! ProfileBO
        cell.imgBG.layer.borderWidth = 0.5
        cell.imgBG.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBG.layer.cornerRadius = 8.0
        cell.imgBG.layer.shadowColor = UIColor.lightGray.cgColor
        cell.lblAddress.text = objList.location
        
        let isBuddy = objList.isBuddy
        if isBuddy == "1" {
            cell.btnAddBuddy.setImage(#imageLiteral(resourceName: "tick_1.png"), for: .normal)
            cell.lblAddBuddy.text = "Buddy Added"
        }
        else {
            cell.btnAddBuddy.setImage(#imageLiteral(resourceName: "plus1.png"), for: .normal)
            cell.lblAddBuddy.text = "Add buddy"
        }
        
        cell.lblNames.text = objList.first_name.capitalized + " " + objList.last_name.capitalized
        let image = UIImage(named:"default_user.png")
        
        cell.imgProfile.kf.setImage(with: URL(string:imgUrl + objList.profile_pic), placeholder: image, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        cell.imgProfile.layer.cornerRadius = cell.imgProfile.frame.size.height/2
        cell.imgProfile.layer.masksToBounds = true
//        cell.imgProfile.image = #imageLiteral(resourceName: "scuba diving.png")
        if objList.arrCertificate.count == 0
        {
            cell.viewTwoImages.isHidden = true
            cell.imgOne.isHidden = true
        }
        else if(objList.arrCertificate.count == 1)
        {
            let val = objList.arrCertificate[0] as! String
            if val == "Scuba_Diving"
            {
                cell.imgOne.image = #imageLiteral(resourceName: "scuba diving.png")
            }
            else{
                cell.imgOne.image = #imageLiteral(resourceName: "cylender.png")
            }
            cell.viewTwoImages.isHidden = true
            cell.imgOne.isHidden = false
        }
        else if (objList.arrCertificate.count == 2)
        {
            cell.viewTwoImages.isHidden = false
            cell.imgOne.isHidden = true
        }
        cell.onClickAddBuddy = {() -> Void in
            self.callAddBuddyWebservice(objList.userId)
        }

        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let objList : ProfileBO = arrSearchList[indexPath.row] as! ProfileBO

        UserParser.callProfileDetailsService(objList.userId) { (status, strMsg, objProfile) in
            if status
            {
                DispatchQueue.main.async {
                    let profileVC:ProfileViewController = self.storyboard!.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
                    profileVC.objProfile = objProfile
                    self.navigationController?.pushViewController(profileVC, animated: true)
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        }
        
    }
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    // MARK: -
    // MARK: -UITextFeild Delegates
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        tap = UITapGestureRecognizer(target: self, action: #selector(self.myviewTapped(_:)))
        self.view.addGestureRecognizer(tap)
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        if textField == txtSearch
        {
            NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(self.callSearchWebservice), object: nil)
            if (timer1.isValid) {
                timer1.invalidate()
            }
            timer1 = Timer(timeInterval: 0.9, target: self, selector: #selector(self.callSearchWebservice), userInfo: nil, repeats: false)
            RunLoop.main.add(timer1, forMode: RunLoopMode.defaultRunLoopMode)
            
            return true
            
            
        }        else
        {
            return true
        }
    }

    func callSearchWebservice()
    {
        let dictVal = NSMutableDictionary()
        dictVal["name"] = txtSearch.text
        TripsParser.callSearchUserService(dictVal) { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    self.arrSearchList = arrList as! NSMutableArray
                    
                    for i in 0 ..< self.arrSearchList.count{
                        let objList : ProfileBO = self.arrSearchList[i] as! ProfileBO
                        let userEmail = UserDefaults.standard.value(forKey: kUserEmail)! as! String
                        if objList.email == userEmail {
                            self.arrSearchList .removeObject(at: i)
                            break
                        }
                    }
                    
                    self.tblViewSearch.isHidden = false
                    self.tblViewSearch.reloadData()
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        }

    }
    
    func callAddBuddyWebservice( _ strBuddyId : String)
    {
        let dictVal = NSMutableDictionary()
        dictVal["buddy_id"] = strBuddyId
        dictVal["device_type"] = "ios"
        UserParser.callAddBuddyService(dictVal, complete: { (status, strMsg) in
            DispatchQueue.main.async {
                Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
            }
        })

    }
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
