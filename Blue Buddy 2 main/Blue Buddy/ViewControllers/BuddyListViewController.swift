//
//  BuddyListViewController.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 09/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit

class BuddyListViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var viewInviteHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewInvite: UIView!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var lblBadgeCount: UILabel!
    var arrBuddId = NSMutableArray()
    var arrBuddies = NSMutableArray()
    var isFromInvite = Bool()
    var indexval = Int()
    var trip_id = String()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        if isFromInvite
        {
            viewInvite.isHidden = false
            viewInviteHgtConstraint.constant = 55
        }
        else
        {
            viewInvite.isHidden = true
            viewInviteHgtConstraint.constant = 0
        }
        self.tblViewList.isHidden = true
        UserParser.callBuddyListService("", complete: { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    self.arrBuddies = arrList
                    if self.arrBuddies.count == 0
                    {
                        self.lblNoData.isHidden = false
                        self.tblViewList.isHidden = true
                    }
                    else{
                        self.tblViewList.reloadData()
                        self.lblNoData.isHidden = true
                        self.tblViewList.isHidden = false
                    }
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    self.lblNoData.isHidden = false
                    self.tblViewList.isHidden = true
                }
            }
        })
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblBadgeCount.text = strCounter
                    }
                }
            }
        }

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    
    @IBAction func onClickBtnInvite(_ sender: UIButton) {
        if arrBuddId.count == 0
        {
            Common.showAlert(message: "Please select your buddy", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let dictVal = NSMutableDictionary()
            dictVal["trip_id"] = trip_id
            dictVal["user_id"] = arrBuddId.componentsJoined(by: ", ")
            dictVal["device_type"] = "ios"
            TripsParser.callInviteBuddyService(dictVal, true) { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
//                                var isFound:Bool = false
                                if let viewControllers = self.navigationController?.viewControllers {
                                    for viewController in viewControllers {
                                        // some process
                                        if viewController.isKind(of: self.indexval == 1 ? SearchTripsViewController.self : MyAccountViewController.self) {
                                          //  isFound = true
                                            if (self.indexval == 1)
                                            {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateTripList"), object: nil)
                                                self.navigationController?.popToViewController(viewController, animated: true)
                                            }
                                            else if (self.indexval == 2){
                                                UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
                                                    DispatchQueue.main.async {
                                                        let tripsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MyTripsViewController") as! MyTripsViewController
                                                        self.navigationController?.pushViewController(tripsVC, animated: true)
                                                    }
                                                })
                                            }
                                            else{
                                                
                                            }
                                            
                                        }
                                    }
                                }
                            }
                                
                            
                        })
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                }
                else
                {
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            }
        }
    }
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrBuddies.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "chatList_Cell", for: indexPath) as! ChatListTableViewCell
        let objValu = arrBuddies[indexPath.row] as! ProfileBO
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.lblName.text = objValu.first_name.capitalized + " " + objValu.last_name.capitalized
        
        if isFromInvite
        {
            cell.imgUserPic.image = #imageLiteral(resourceName: "square.png")
            cell.imgUserPic.isHidden = true
            cell.imgCheck.isHidden = false
        }
        else{
            cell.imgUserPic.isHidden = false
            cell.imgCheck.isHidden = true
            cell.imgUserPic.kf.setImage(with: URL(string:objValu.profile_pic), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
            cell.imgUserPic.layer.cornerRadius = cell.imgUserPic.frame.size.height/2
            cell.imgUserPic.clipsToBounds = true
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 95
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let objValu = arrBuddies[indexPath.row] as! ProfileBO
        let cell = tableView.cellForRow(at: indexPath) as! ChatListTableViewCell
        if isFromInvite
        {
            if arrBuddId.contains(objValu.userId)
            {
                arrBuddId.remove(objValu.userId)
                cell.imgCheck.image = #imageLiteral(resourceName: "square.png")            }
            else{
                arrBuddId.add(objValu.userId)
                cell.imgCheck.image = #imageLiteral(resourceName: "check.png")
            }
        }
        else{
            UserParser.callProfileDetailsService(objValu.userId) { (status, strMsg, objProfile) in
                if status
                {
                    DispatchQueue.main.async {
                        let profileVC:ProfileViewController = self.storyboard!.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
                        profileVC.objProfile = objProfile
                        self.navigationController?.pushViewController(profileVC, animated: true)
                    }
                }
            }
        }
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
