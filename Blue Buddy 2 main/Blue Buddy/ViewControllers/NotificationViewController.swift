//
//  NotificationViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 1/2/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit

class NotificationViewController: UIViewController, UITableViewDataSource, UITableViewDelegate{

    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var lblNoData: UILabel!
    var prototypeCell = NotificationsTableViewCell()
    var arrNotifications = NSMutableArray()
    override func viewDidLoad() {
        super.viewDidLoad()
        tblViewList.isHidden = true
        UserParser.callMyNotificationService([ : ]) { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async { 
                    if arrList.count != 0
                    {
                        self.arrNotifications = arrList as! NSMutableArray
                        self.tblViewList.isHidden = false
                        self.lblNoData.isHidden = true
                        self.tblViewList.reloadData()
                    }
                    else
                    {
                        self.tblViewList.isHidden = true
                        self.lblNoData.isHidden = false
                    }
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.lblNoData.isHidden = false
                    self.tblViewList.isHidden = true
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        }
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func onClickbtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrNotifications.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "notification_Cell", for: indexPath) as! NotificationsTableViewCell
        let objVal = arrNotifications[indexPath.row] as! NotificationBO
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.lblDesc.text = objVal.message
        cell.imgUser.layer.cornerRadius = 5.0
        if objVal.viewed_at == "1"
        {
            cell.imgBg.backgroundColor = .lightGray
        }
        else{
            cell.imgBg.backgroundColor = .white
        }
        cell.imgUser.kf.setImage(with: URL(string:objVal.profile_pic), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        if objVal.notify_for == "invite_trip"
        {
            cell.btnAccept.isHidden = true
            cell.btnIgnore.isHidden = true
            cell.btnIgnoreHgtConstraint.constant = 0
            cell.btnAcceptHgtConstraint.constant = 0
            cell.lblActivity.text = objVal.first_name.capitalized + " " + objVal.last_name.capitalized
            cell.lblName.text = ""
        }
        else if(objVal.notify_for == "join_trip")
        {
           
            if(objVal.is_accepted == "0")
            {
                cell.btnAccept.isHidden = false
                cell.btnIgnore.isHidden = false
                cell.btnIgnoreHgtConstraint.constant = 35
                cell.btnAcceptHgtConstraint.constant = 35
                
                cell.onClickSelectedIndex = {(isAccepted : Bool) -> Void in
                    self.callAcceptRejectService(objVal, isAccepted)
                }
            }
            else{
                cell.btnAccept.isHidden = true
                cell.btnIgnore.isHidden = true
                cell.btnIgnoreHgtConstraint.constant = 0
                cell.btnAcceptHgtConstraint.constant = 0
            }
            cell.lblActivity.text = objVal.activity_type.capitalized
            cell.lblName.text = objVal.first_name.capitalized + " " + objVal.last_name.capitalized
        }
        else if(objVal.notify_for == "buddy_up")
        {
            if(objVal.is_accepted == "0")
            {

            cell.btnAccept.isHidden = false
            cell.btnIgnore.isHidden = false
            cell.btnIgnoreHgtConstraint.constant = 35
            cell.btnAcceptHgtConstraint.constant = 35
                cell.onClickSelectedIndex = {(isAccepted : Bool) -> Void in
                    self.callAcceptBuddyService(objVal, isAccepted)
                }
            }
            else{
                cell.btnAccept.isHidden = true
                cell.btnIgnore.isHidden = true
                cell.btnIgnoreHgtConstraint.constant = 0
                cell.btnAcceptHgtConstraint.constant = 0
            }
            cell.lblActivity.text = objVal.create_date
            cell.lblName.text = objVal.first_name.capitalized + " " + objVal.last_name.capitalized
            cell.lblDesc.text = objVal.first_name.capitalized + " wants to be your buddy."
        }

        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let objVal = arrNotifications[indexPath.row] as! NotificationBO
        if objVal.notify_for == "invite_trip"
        {
            TripsParser.callTripsDetailsService(["event_id" : objVal.for_id], complete: { (status, strMsg, objValue, objTripData) in
                if status
                {
                    DispatchQueue.main.async {
                        UserParser.callViewedNotificationService(objVal.notification_id, complete: { (status, strMsg) in
                            DispatchQueue.main.async{
                                objVal.viewed_at = "1"
                                let tripDetailsVC = self.storyboard!.instantiateViewController(withIdentifier: "TripDetailsViewController") as! TripDetailsViewController
                                tripDetailsVC.objTripDetail = objTripData
                                tripDetailsVC.arrParticipants = objValue as! NSMutableArray
                                self.navigationController?.pushViewController(tripDetailsVC, animated: true)
                            }
                        })
                    }
                }
                else
                {
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            })

        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        self.prototypeCell = tblViewList.dequeueReusableCell(withIdentifier: "notification_Cell")! as! NotificationsTableViewCell
        
        let objVal = arrNotifications[indexPath.row] as! NotificationBO
        if objVal.notify_for == "invite_trip" || objVal.is_accepted == "1"
        {
            return 132
        }
        else{
            return 177
        }
    }

    
    func callAcceptRejectService( _ objVal: NotificationBO, _ isAccepted : Bool)
    {
        let dictVal = NSMutableDictionary()
        dictVal["event_id"] = objVal.for_id
        dictVal["status"] = isAccepted == true ? "1" : "0"
        dictVal["participant_id"] = objVal.requestor_id
        dictVal["notification_id"] = objVal.notification_id
        dictVal["device_type"] = "ios"
        TripsParser.callAcceptParticipateInTripsService(dictVal) { (status, strMsg) in
            if status
            {
                DispatchQueue.main.async {
                    objVal.is_accepted = isAccepted == true ? "1" : "0"
                    objVal.message = isAccepted == true ? objVal.first_name.capitalized + " is now a participant of your trip!" : "You have rejected " + objVal.first_name.capitalized + "'s trip request!"
                    self.tblViewList.reloadData()
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        }
    }
    
    func callAcceptBuddyService( _ objVal: NotificationBO, _ isAccepted : Bool)
    {
        let dictVal = NSMutableDictionary()
        dictVal["user_id"] = objVal.user_id
        dictVal["status"] = isAccepted == true ? "1" : "0"
        dictVal["notification_id"] = objVal.notification_id
        dictVal["device_type"] = "ios"
        UserParser.callAcceptBuddyService(dictVal) { (status, strMsg) in
            if status
            {
                DispatchQueue.main.async {
                    objVal.is_accepted = isAccepted == true ? "1" : "0"
                    objVal.message = isAccepted == true ? "You are now buddy with " + objVal.first_name.capitalized : "You have rejected " + objVal.first_name.capitalized + "'s buddy request!"
                    self.tblViewList.reloadData()
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
