//
//  TripDetailsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/4/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class TripDetailsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var img1: UIImageView!
    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnInterested: UIButton!
  //  @IBOutlet weak var contentViewHgtConstraint: NSLayoutConstraint!
  //  @IBOutlet weak var viewTblHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblHeaderTitle: UILabel!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var lblTripName: UILabel!
    @IBOutlet weak var lblPostedBy: UILabel!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblLine2: UILabel!
    @IBOutlet weak var lblLine1: UILabel!
    @IBOutlet weak var btnParticipants: UIButton!
    @IBOutlet weak var btnDetails: UIButton!
    @IBOutlet weak var selectionView1: UIView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    
    @IBOutlet weak var selectionView2: UIView!
    
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var lblNoParticipants: UILabel!
    @IBOutlet weak var txtDetails: UITextView!
    @IBOutlet weak var scrollViewBtm: UIScrollView!
    @IBOutlet weak var lblLine4: UILabel!
    @IBOutlet weak var btnParticipants1: UIButton!
    @IBOutlet weak var lblLine3: UILabel!
    @IBOutlet weak var btnDetails1: UIButton!
    @IBOutlet weak var lblParticipantsNo: UILabel!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    var objTripDetail = TripsDetailsBO()
    var selectedIndex: Int?
    var indexVal = Int()
    var arrParticipants = NSMutableArray()
    var isMyTrip = Bool()
    override func viewDidLoad() {
        super.viewDidLoad()

        lblLine2.isHidden = true
        lblLine3.isHidden = false
        lblLine4.isHidden = true
        img1.isHidden = true
        btnParticipants1.titleLabel?.textColor = .darkGray
        btnParticipants.titleLabel?.textColor = .darkGray
        lblLine1.isHidden = false
        lblParticipantsNo.layer.cornerRadius = lblParticipantsNo.frame.size.height/2
        lblParticipantsNo.clipsToBounds = true
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
               selectedIndex = 1
        lblDate.text = objTripDetail.event_date
        lblPostedBy.text = objTripDetail.user_name
        lblTripName.text = objTripDetail.activity
        lblParticipantsNo.text = objTripDetail.no_of_participant
        txtDetails.text = objTripDetail.trip_desc
        lblAddress.text = objTripDetail.event_loc
        
        isMyTrip = objTripDetail.user_id == LOGIN_CONSTANT.objProfile.userId ? true : false
        
        lblNoData.isHidden = true
        imgProfile.kf.setImage(with: URL(string:imgUrl + objTripDetail.user_pic), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        imgProfile.layer.cornerRadius = imgProfile.frame.size.width/2
        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtDetails.frame.width, font: txtDetails.font!, string: objTripDetail.trip_desc) + 25
        txtDescHgtConstraint.constant = valHgt < 85 ? 85 : valHgt
        if objTripDetail.no_of_participant == "0"
        {
            tblViewList.isHidden = true
            lblNoData.isHidden = false
        }
        else
        {
            tblViewList.isHidden = false
        }
        if objTripDetail.is_participated || objTripDetail.user_id == LOGIN_CONSTANT.objProfile.userId
        {
            btnInterested.isHidden = true
        }
        else{
            btnInterested.isHidden = false
        }
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
    
    @IBAction func onClickBtnIMIn(_ sender: UIButton) {
        
        TripsParser.callParticipateInTripsService(["event_id" : objTripDetail.event_id, "device_type" : "ios"], complete: { (status, strMsg) in
            if status
            {
                DispatchQueue.main.async {
                    self.btnInterested.isHidden = true
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
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
    @IBAction func onClickBtnProfile(_ sender: UIButton) {
        self.callProfileService(objTripDetail.user_id)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickBtnSelectionHeader(_ sender: UIButton) {
        if  sender.tag == 1 {
            lblLine2.isHidden = true
            lblLine1.isHidden = false
            btnParticipants.titleLabel?.textColor = .darkGray
            lblLine3.isHidden = false
            lblLine4.isHidden = true
            btnParticipants1.titleLabel?.textColor = .darkGray
            selectedIndex = 1
        }
        else{
            lblLine1.isHidden = true
            lblLine2.isHidden = false
            btnDetails1.titleLabel?.textColor = .darkGray
            lblLine3.isHidden = true
            lblLine4.isHidden = false
            btnDetails.titleLabel?.textColor = .darkGray
            selectedIndex = 2
        }
        let pageWidth:CGFloat = scrollViewBtm.frame.width
        let contentOffset:CGFloat = scrollViewBtm.contentOffset.x
        
        var slideToX = contentOffset + pageWidth
        
        if  sender.tag == 1
        {
            slideToX = 0
        }
        else{
            slideToX = contentOffset + pageWidth
        }
        scrollViewBtm.scrollRectToVisible(CGRect(x:slideToX, y:0, width:pageWidth, height:scrollViewBtm.frame.height), animated: true)
    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrParticipants.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "chatList_Cell", for: indexPath) as! ChatListTableViewCell
        let objValu = arrParticipants[indexPath.row] as! ProfileBO
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.lblInitial.layer.cornerRadius = cell.lblInitial.frame.size.width/2
        cell.lblInitial.clipsToBounds = true
        cell.lblName.text = objValu.first_name.capitalized + " " + objValu.last_name.capitalized
        cell.lblInitial.isHidden = true
        cell.imgUserPic.kf.setImage(with: URL(string:imgUrl + objValu.profile_pic), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        
        if isMyTrip {
            cell.btnRemove.isHidden = false
        }
        else {
            cell.btnRemove.isHidden = true
        }
        cell.onClickBtnDelete = {() -> Void in
            
            let alert = UIAlertController(title: "BLUE BUDDY",
                                          message: "Are you sure you want to remove the participant?",
                                          preferredStyle: UIAlertControllerStyle.alert)
            let settingAction = UIAlertAction(title: "Yes" , style: .default, handler: {(_ action: UIAlertAction) -> Void in
                self.removeParticipantFromTrip(participant_id: objValu.userId, rowNumber: indexPath.row)
            })
            
            let cancelAction = UIAlertAction(title: "No", style: .cancel, handler: nil)
            
            alert.addAction(settingAction)
            alert.addAction(cancelAction)
            self.present(alert, animated: true, completion: nil)
            
            
        }
        
        return cell
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 95
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let objValu = arrParticipants[indexPath.row] as! ProfileBO
        self.callProfileService(objValu.userId)
    }

    func removeParticipantFromTrip(participant_id : String, rowNumber : Int) {
        TripsParser.callDeleteParticipationInTripsService(["event_id" : objTripDetail.event_id, "device_type" : "ios", "participant_id" :participant_id], complete: { (status, strMsg) in
            if status
            {
                DispatchQueue.main.async {
                    //                            cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
                    self.arrParticipants.removeObject(at: rowNumber)
                    self.tblViewList.reloadData()
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
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
    
    // MARK: -
    // MARK: - ScrollView Delegates
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollViewMain.contentOffset.y >= viewHeader.frame.size.height
        {
            selectionView2.isHidden = false
            self.view.bringSubview(toFront: selectionView2)
            lblHeaderTitle.text = objTripDetail.activity
            scrollViewMain.isScrollEnabled = false
            scrollViewMain.setContentOffset(CGPoint(x: 0,y: viewHeader.frame.size.height), animated: true)
            scrollViewMain.isScrollEnabled = true
            self.tblViewList.isScrollEnabled = true
        }
        else{
            self.tblViewList.isScrollEnabled  = false
            selectionView2.isHidden = true
            lblHeaderTitle.text = "TRIP DETAILS"
        }
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView){
        // Test the offset and calculate the current page after scrolling ends
        
        if scrollView == scrollViewBtm
        {
            let pageWidth:CGFloat = scrollView.frame.width
            let currentPage:CGFloat = floor((scrollView.contentOffset.x-pageWidth/2)/pageWidth)+1
            if  Int(currentPage) == 0 {
                lblLine2.isHidden = true
                lblLine1.isHidden = false
                lblLine3.isHidden = false
                lblLine4.isHidden = true
                btnParticipants.titleLabel?.textColor = .darkGray
                btnParticipants1.titleLabel?.textColor = .darkGray
               
                selectedIndex = 1
            }
            else{
                lblLine1.isHidden = true
                lblLine2.isHidden = false
                btnDetails1.titleLabel?.textColor = .darkGray
                lblLine3.isHidden = true
                lblLine4.isHidden = false
                btnDetails.titleLabel?.textColor = .darkGray
                selectedIndex = 2
            }
        }
        else if (scrollView == scrollViewMain)
        {
        //    contentViewHgtConstraint.constant = selectedIndex == 1 ? selectionView2.frame.maxY + txtDetails.frame.size.height : selectionView2.frame.maxY + (5 * 95)
        }
    }
    
    func callProfileService( _ strId: String)
    {
        UserParser.callProfileDetailsService(strId) { (status, strMsg, objProfile) in
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


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
