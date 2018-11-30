//
//  MyTripsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/20/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MyTripsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tblViewJoinedTrips: UITableView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblNo: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var lblLine2: UILabel!
    @IBOutlet weak var btnJoinedTrips: UIButton!
    @IBOutlet weak var lblLine1: UILabel!
    @IBOutlet weak var btnTrips: UIButton!
    var selectedIndex = 1
    var prototypeCell = MyTripsTableViewCell()
    override func viewDidLoad() {
        super.viewDidLoad()

        lblLine2.isHidden = true
        btnJoinedTrips.titleLabel?.textColor = .darkGray
        lblNo.layer.cornerRadius = lblNo.frame.size.height/2
        lblNo.clipsToBounds = true
        tblViewList.reloadData()
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblNo.text = strCounter
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
    
    // MARK: -
    // MARK: - UIButton Action
    @IBAction func onClickBtnTrips(_ sender: UIButton) {
        
        DispatchQueue.main.async {
            UIView.animate(withDuration: 0.2, delay: 0, options: UIViewAnimationOptions.curveEaseOut, animations: {
                self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                if  sender.tag == 1 {
                    self.btnTrips.titleLabel?.textColor = .darkGray
                    self.lblLine1.isHidden = false
                    self.lblLine2.isHidden = true
                    self.selectedIndex = 1
                    self.scrollViewMain.contentOffset.x = CGFloat(0)
                    self.tblViewList.reloadData()
                    self.tblViewList.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                }
                else{
                    self.btnJoinedTrips.titleLabel?.textColor = .darkGray
                    self.lblLine1.isHidden = true
                    self.lblLine2.isHidden = false
                    self.selectedIndex = 2
                    self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                    self.tblViewJoinedTrips.reloadData()
                    self.tblViewJoinedTrips.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                }
            }, completion: nil)
        }
    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if selectedIndex == 1
        {
            return LOGIN_CONSTANT.objProfile.arrTrips.count
        }
        else{
            return LOGIN_CONSTANT.objProfile.arrParticipation.count
        }
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "myTrips_Cell", for: indexPath) as! MyTripsTableViewCell
        
        let objTrip: TripsDetailsBO = selectedIndex == 1 ? LOGIN_CONSTANT.objProfile.arrTrips[indexPath.row] as! TripsDetailsBO : LOGIN_CONSTANT.objProfile.arrParticipation[indexPath.row] as! TripsDetailsBO

        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
        
        cell.lblNo.layer.cornerRadius = cell.lblNo.frame.size.width/2
        cell.lblNo.clipsToBounds = true
        cell.lblNo.text = objTrip.no_of_participant
        cell.btnEdit.isHidden = selectedIndex == 2 ? true : false
        cell.btnInviteBuddy.isHidden = selectedIndex == 2 ? true : false
        cell.lblInvite.isHidden = selectedIndex == 2 ? true : false
        cell.lblName.text = objTrip.activity
        cell.lblDate.text = objTrip.event_date
        cell.lblDesc.text = objTrip.trip_desc
        cell.lblAddress.text = objTrip.event_loc
        
        print(objTrip.cellHgt)
     //   let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: cell.lblDesc.frame.width, font: cell.lblDesc.font!, string: objTrip.trip_desc) + 25
        cell.lblDescHgtConstraint.constant = CGFloat(objTrip.cellHgt)
        
        cell.onClickDetails = {() -> Void in
            TripsParser.callTripsDetailsService(["event_id" : objTrip.event_id], complete: { (status, strMsg, objValue, objTripData) in
                if status
                {
                    DispatchQueue.main.async {
                        //                        let myViewController = DetailsViewController(nibName: "DetailsViewController", bundle: nil)
                        //                        self.navigationController?.pushViewController(myViewController, animated: true)
                        let tripDetailsVC = self.storyboard!.instantiateViewController(withIdentifier: "TripDetailsViewController") as! TripDetailsViewController
                        let val = objTrip 
                        val.is_participated = true
                        tripDetailsVC.objTripDetail = val
                        tripDetailsVC.arrParticipants = objValue as! NSMutableArray
                        self.navigationController?.pushViewController(tripDetailsVC, animated: true)
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
        cell.onClickEdit = {() -> Void in
            let editVC = self.storyboard!.instantiateViewController(withIdentifier: "CreateTripViewController") as! CreateTripViewController
            editVC.objTrips = objTrip
            editVC.isFromUpdate = true
            editVC.onClickDelete = {() -> Void in
                LOGIN_CONSTANT.objProfile.arrTrips.remove(objTrip)
                self.tblViewList.reloadData()
            }
            editVC.onClickEdit = {(objValue) -> Void in
                LOGIN_CONSTANT.objProfile.arrTrips.remove(objTrip)
                LOGIN_CONSTANT.objProfile.arrTrips.add(objValue)
                self.tblViewList.reloadData()
            }
            self.navigationController?.pushViewController(editVC, animated: true)
            
            }
        cell.onClickInvite = {() -> Void in
            DispatchQueue.main.async {
                let inviteVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "InviteBuddiesViewController") as! InviteBuddiesViewController
                inviteVC.strTripId = objTrip.event_id
                inviteVC.value = 2
                self.navigationController?.pushViewController(inviteVC, animated: true)
            }

        }
        return cell
    }
   
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        self.prototypeCell = tblViewList.dequeueReusableCell(withIdentifier: "myTrips_Cell")! as! MyTripsTableViewCell
        let objVal:TripsDetailsBO = selectedIndex == 1 ? LOGIN_CONSTANT.objProfile.arrTrips[indexPath.row] as! TripsDetailsBO : LOGIN_CONSTANT.objProfile.arrParticipation[indexPath.row] as! TripsDetailsBO
        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblDesc.frame.size.width, font: prototypeCell.lblDesc.font, string: objVal.trip_desc)
        objVal.cellHgt = cellHgt + 25
        return 237 + cellHgt
    }
 
    
    // MARK: -
    // MARK: - UIScrollViewDelegates
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        
        if scrollView == scrollViewMain {
            DispatchQueue.main.async {
                UIView.animate(withDuration: 0.2, delay: 0, options: UIViewAnimationOptions.curveEaseOut, animations: {
                    //   self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                    let pageNumber = round(scrollView.contentOffset.x / scrollView.frame.size.width)
                    if pageNumber == 0 {
                        self.btnTrips.titleLabel?.textColor = .black
                        self.btnJoinedTrips.titleLabel?.textColor = .darkGray
                        self.lblLine1.isHidden = false
                        self.lblLine2.isHidden = true
                        self.selectedIndex = 1
                        self.scrollViewMain.contentOffset.x = CGFloat(0)
                        self.tblViewList.reloadData()
                        self.tblViewList.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                    }
                    else{
                        self.btnTrips.titleLabel?.textColor = .darkGray
                        self.btnJoinedTrips.titleLabel?.textColor = .black
                        self.lblLine1.isHidden = true
                        self.lblLine2.isHidden = false
                        self.selectedIndex = 2
                        self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                        self.tblViewJoinedTrips.reloadData()
                        self.tblViewJoinedTrips.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                    }
                }, completion: nil)
            }
        }
        else {
            
            //something else
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
