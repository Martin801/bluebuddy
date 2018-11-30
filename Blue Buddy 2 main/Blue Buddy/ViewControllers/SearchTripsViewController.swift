//
//  SearchTripsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/27/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel

class SearchTripsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , UIScrollViewDelegate, FSCalendarDataSource, FSCalendarDelegate, UIGestureRecognizerDelegate , UITextFieldDelegate, GMSPlacePickerViewControllerDelegate{


    @IBOutlet weak var viewCurrentLoc: UIView!
    @IBOutlet weak var viewOtherLoc: UIView!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var lblSliderVal: UILabel!
    @IBOutlet weak var viewSlider: UISlider!
    @IBOutlet weak var viewLocHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewLoc: UIView!
    @IBOutlet weak var viewBtn: UIView!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var lblToDate: UILabel!
    @IBOutlet weak var lblFromDate: UILabel!
    @IBOutlet weak var calanderConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblYear: UILabel!
    @IBOutlet weak var viewCalendar: FSCalendar!
    @IBOutlet weak var lblNotificationBadge: UILabel!
    @IBOutlet weak var viewPopUpDate: UIView!
    @IBOutlet weak var viewDate: UIView!
    @IBOutlet weak var tblListHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnSearch: UIButton!
    @IBOutlet weak var imgFromDate: UIImageView!
    @IBOutlet weak var imgToDate: UIImageView!
    @IBOutlet weak var btnOtherLoc: UIButton!
    @IBOutlet weak var btnCurrentLoc: UIButton!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var tblList: UITableView!
    var arrTripList = NSMutableArray()
    var selectedIndex = 0
    var dateSelectedIndex = 1
    var strCategory = NSString ()
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var lengthyLabel: MarqueeLabel?
    var prototypeCell = SerachTripsTableViewCell()
    var timer = Timer()
    fileprivate lazy var dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy/MM/dd"
        return formatter
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        let myColor = UIColor.darkGray
        lblNotificationBadge.layer.cornerRadius = lblNotificationBadge.frame.size.height/2
        lblNotificationBadge.clipsToBounds = true
        imgFromDate.layer.cornerRadius = 5.0
        imgFromDate.layer.borderWidth = 2.0
        imgFromDate.layer.borderColor = myColor.cgColor
        imgToDate.layer.cornerRadius = 5.0
        imgToDate.layer.borderWidth = 2.0
        imgToDate.layer.borderColor = myColor.cgColor
        
        btnCurrentLoc.layer.borderColor         = myColor.cgColor
        btnCurrentLoc.layer.borderWidth         = 1.0
        btnCurrentLoc.layer.cornerRadius        = 5.0
        btnOtherLoc.layer.borderColor             = myColor.cgColor
        btnOtherLoc.layer.borderWidth             = 1.0
        btnOtherLoc.layer.cornerRadius            = 5.0

        btnSearch.layer.cornerRadius = 5.0
        btnSearch.layer.borderWidth = 2.0
        btnSearch.layer.borderColor = myColor.cgColor
        viewPopUpDate.isHidden = true
        viewCalendar.appearance.headerMinimumDissolvedAlpha = 0;
        viewCalendar.appearance.caseOptions = .weekdayUsesSingleUpperCase;
        viewCalendar.firstWeekday = 2;
        self.viewPopUpDate.frame = CGRect(x: 0, y: 0, width: (APP_DELEGATE.window?.frame.size.width)!, height: (APP_DELEGATE.window?.frame.size.height)!)
        UIApplication.shared.keyWindow!.addSubview(self.viewPopUpDate)
        viewPopUpDate.translatesAutoresizingMaskIntoConstraints = true
        lblNoData.isHidden = true
        
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        
        tblListHgtConstraint.constant = view.frame.size.height - (viewDate.frame.maxY + 65)
        
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblNotificationBadge.text = strCounter
                    }
                }
            }
        }
        NotificationCenter.default.addObserver(self, selector: #selector(SearchTripsViewController.callTripList), name: NSNotification.Name(rawValue: "updateTripList"), object: nil)
        self.callTripList()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        self.callTripList()
    }
    
    // MARK: -
    // MARK: - UIButton Action
    
    @IBAction func onClickBtnPopUpDates(_ sender: UIButton) {
        if sender.tag == 1 {
            
        }
        else{
            if dateSelectedIndex == 1
            {
                lblFromDate.text = self.dateFormatter.string(from: viewCalendar.selectedDate!)
            }
            else{
                lblToDate.text = self.dateFormatter.string(from: viewCalendar.selectedDate!)
            }
        }
        viewPopUpDate.isHidden = true
    }
    
    /*
 
       
    */
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)

    }
    @IBAction func onClickBtnSelectionHeader(_ sender: UIButton) {
        
        sender.isSelected = !sender.isSelected
        if sender.isSelected
        {
            tblListHgtConstraint.constant = view.frame.size.height - (viewDate.frame.maxY + 65 + 65)
            switch sender.tag {
            case 1:
                self.btnCurrentLoc.setTitleColor(.white, for: .selected)
                self.btnCurrentLoc.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnOtherLoc.isSelected = false
                self.btnOtherLoc.setTitleColor(.black, for: .normal)
                self.btnOtherLoc.backgroundColor = .white
                selectedIndex = 1
                viewLoc.isHidden = false
                viewSlider.value = 1
                lblSliderVal.text = "1" + "km"
                self.viewCurrentLoc.isHidden = false
                self.viewOtherLoc.isHidden = true
                viewLocHgtConstraint.constant = 65
                self.callTripList()
                self.view.bringSubview(toFront: self.viewCurrentLoc)
                
            case 2:
                self.btnOtherLoc.setTitleColor(.white, for: .selected)
                self.btnOtherLoc.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnCurrentLoc.isSelected = false
                self.btnCurrentLoc.setTitleColor(.black, for: .normal)
                self.btnCurrentLoc.backgroundColor = .white
                selectedIndex = 2
                viewLoc.isHidden = false
                self.viewOtherLoc.isHidden = false
                txtLocation.leftView = nil
                txtLocation.placeholder = "Enter Location"
                self.viewCurrentLoc.isHidden = true
                viewLocHgtConstraint.constant = 65
                self.view.bringSubview(toFront: self.viewOtherLoc)
                
            default:
                selectedIndex = 0
                btnCurrentLoc.isSelected = false
                btnOtherLoc.isSelected = false
                tblListHgtConstraint.constant = view.frame.size.height - (viewDate.frame.maxY + 65)
                viewLoc.isHidden = true
                self.btnOtherLoc.setTitleColor(.black, for: .normal)
                self.btnOtherLoc.backgroundColor = .white
                self.btnCurrentLoc.setTitleColor(.black, for: .normal)
                self.btnCurrentLoc.backgroundColor = .white

                self.viewOtherLoc.isHidden = true
                self.viewCurrentLoc.isHidden = true
                viewLocHgtConstraint.constant = 0
                self.callTripList()
                
                break
            }
        }
        else
        {
            tblListHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY + 45)
            selectedIndex = 0
            viewLoc.isHidden = true
            btnCurrentLoc.isSelected = false
            btnOtherLoc.isSelected = false
            self.btnOtherLoc.setTitleColor(.black, for: .normal)
            self.btnOtherLoc.backgroundColor = .white
            self.btnCurrentLoc.setTitleColor(.black, for: .normal)
            self.btnCurrentLoc.backgroundColor = .white
            self.viewOtherLoc.isHidden = true
            self.viewCurrentLoc.isHidden = true
            viewLocHgtConstraint.constant = 0
            self.callTripList()
        }

        
    }
    
    @IBAction func onChangeSliderValue(_ sender: UISlider) {
        let value = Int (sender.value)
        lblSliderVal.text = String (value) + " km"
        selectedIndex = 1
        NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(self.callTripList), object: nil)
        if (timer.isValid) {
            timer.invalidate()
        }
        timer = Timer(timeInterval: 0.9, target: self, selector: #selector(self.callTripList), userInfo: nil, repeats: false)
        RunLoop.main.add(timer, forMode: RunLoopMode.defaultRunLoopMode)
    }
    @IBAction func onClickBtnDate(_ sender: UIButton) {
        dateSelectedIndex = sender.tag
        viewPopUpDate.isHidden = false
        
    }
    @IBAction func onClickBtnSearch(_ sender: UIButton) {
        if lblFromDate.text == "From Date"
        {
            
        }
        else if(lblToDate.text == "To Date")
        {
            
        }
        else{
            self.callTripList()
        }
    }
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        let createTripVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CreateTripViewController") as! CreateTripViewController
        createTripVC.indexval = 1
        self.navigationController?.pushViewController(createTripVC, animated: true)
        
    }

    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    @IBAction func onClickTapOutsidePopUp(_ sender: UITapGestureRecognizer) {
        viewPopUpDate.isHidden = true
        
    }

    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrTripList.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "serachTrips_Cell", for: indexPath) as! SerachTripsTableViewCell
        
        let objTrip: TripsDetailsBO = arrTripList[indexPath.row] as! TripsDetailsBO
        cell.lblName.text = objTrip.activity
        cell.lblPosterName.text = objTrip.user_name
        cell.lblDate.text = objTrip.event_date
        cell.lblDesc.text = objTrip.trip_desc
        cell.lblAddress.text = objTrip.event_loc
        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: cell.lblDesc.frame.width, font: cell.lblDesc.font!, string: objTrip.trip_desc) + 25
        cell.lblDesHgtConstraint.constant = valHgt
        cell.imgUser.kf.setImage(with: URL(string:imgUrl + objTrip.user_pic), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        cell.imgUser.layer.cornerRadius = cell.imgUser.frame.size.width/2
        cell.imgUser.clipsToBounds = true
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
        if !objTrip.is_editable && objTrip.is_participated
        {
            cell.btnEdit.isHidden = false
//            cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
            cell.btnEdit.setTitle("UNREQUEST", for: .normal)
            cell.btnEdit.isEnabled = true
            cell.imgFlag.isHidden = false
            cell.btnFlag.isHidden = false
            cell.imgFlag.image = objTrip.is_flagged ? #imageLiteral(resourceName: "report_flag.png") : #imageLiteral(resourceName: "flag.png")
            cell.btnAdd.isHidden = true
            cell.lablInviteBuddy.isHidden = true
        }
        else
        {
            if objTrip.is_editable
            {
                cell.btnEdit.isHidden = false
                cell.btnEdit.setTitle("EDIT", for: .normal)
                cell.btnEdit.isEnabled = true
                cell.imgFlag.isHidden = true
                cell.btnFlag.isHidden = true
                cell.btnAdd.isHidden = true
                cell.lablInviteBuddy.isHidden = true
            }
            else
            {
                cell.btnEdit.isHidden = false
                cell.btnEdit.isEnabled = true
                cell.btnEdit.setTitle("I'M IN", for: .normal)
                cell.btnEdit.setBackgroundImage(#imageLiteral(resourceName: "Rectangle 90.png"), for: .normal)
                cell.imgFlag.isHidden = false
                cell.btnFlag.isHidden = false
                cell.imgFlag.image = objTrip.is_flagged ? #imageLiteral(resourceName: "report_flag.png") : #imageLiteral(resourceName: "flag.png")
                cell.btnAdd.isHidden = true
                cell.lablInviteBuddy.isHidden = true
            }
        }
        cell.onClickDetails = {() -> Void in
            TripsParser.callTripsDetailsService(["event_id" : objTrip.event_id], complete: { (status, strMsg, objValue, objTripData) in
                if status
                {
                    DispatchQueue.main.async {
//                        let myViewController = DetailsViewController(nibName: "DetailsViewController", bundle: nil)
//                        self.navigationController?.pushViewController(myViewController, animated: true)
                        let tripDetailsVC = self.storyboard!.instantiateViewController(withIdentifier: "TripDetailsViewController") as! TripDetailsViewController
                        tripDetailsVC.objTripDetail = objTrip
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
        
        cell.onClickParticipate = {() -> Void in
            if objTrip.is_editable
            {
                let editVC = self.storyboard!.instantiateViewController(withIdentifier: "CreateTripViewController") as! CreateTripViewController
                editVC.indexval = 1
                editVC.objTrips = objTrip
                editVC.isFromUpdate = true
                editVC.onClickDelete = {() -> Void in
                    self.arrTripList.remove(objTrip)
                    self.tblList.reloadData()
                }
                editVC.onClickEdit = {(objValue) -> Void in
                    self.arrTripList.remove(objTrip)
                    self.arrTripList.add(objValue)
                    self.tblList.reloadData()
                }
                self.navigationController?.pushViewController(editVC, animated: true)
            }
            else if objTrip.is_participated {
                
                
                let alert = UIAlertController(title: "BLUE BUDDY",
                                              message: "Are you sure you want to remove your participation?",
                                              preferredStyle: UIAlertControllerStyle.alert)
                let settingAction = UIAlertAction(title: "Yes" , style: .default, handler: {(_ action: UIAlertAction) -> Void in
                    
                    
                    TripsParser.callDeleteParticipationInTripsService(["event_id" : objTrip.event_id, "device_type" : "ios", "participant_id" :LOGIN_CONSTANT.objProfile.userId], complete: { (status, strMsg) in
                        if status
                        {
                            DispatchQueue.main.async {
                                //                            cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
                                cell.btnEdit.setTitle("I'M IN", for: .normal)
                                cell.btnEdit.isEnabled = true
                                objTrip.is_participated = false
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
                    
                    
                })
                
                let cancelAction = UIAlertAction(title: "No", style: .cancel, handler: nil)
                
                alert.addAction(settingAction)
                alert.addAction(cancelAction)
                self.present(alert, animated: true, completion: nil)
                
                
                
            }
            else{
                TripsParser.callParticipateInTripsService(["event_id" : objTrip.event_id, "device_type" : "ios"], complete: { (status, strMsg) in
                    if status
                    {
                        DispatchQueue.main.async {
//                            cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
                            cell.btnEdit.setTitle("UNREQUEST", for: .normal)
                            cell.btnEdit.isEnabled = true
                            objTrip.is_participated = true
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
        }
        cell.onClickReport = {() -> Void in
            
            let alert = UIAlertController(title: "BLUE BUDDY", message: "Are you sure you want to report?", preferredStyle: UIAlertControllerStyle.alert)
            let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                let dictVal = NSMutableDictionary()
                dictVal["type"] = "trip"
                dictVal["type_id"] = objTrip.event_id
                TripsParser.callReportService(dictVal, complete: { (status, strMsg) in
                    if status
                    {
                        cell.imgFlag.image = #imageLiteral(resourceName: "report_flag.png")
                        objTrip.is_flagged = true
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                    else{
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                    
                })
            })
            let cancelAction = UIAlertAction(title: "NO", style: .cancel, handler: nil)
            alert.addAction(okAction)
            alert.addAction(cancelAction)
            self.present(alert, animated: true, completion: nil)
        }
        cell.onClickProfile = {() -> Void in
            UserParser.callProfileDetailsService(objTrip.user_id, complete: { (status, strMsg, objPro) in
                if status
                {
                    DispatchQueue.main.async {
                        let profileVC:ProfileViewController = self.storyboard!.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
                        profileVC.objProfile = objPro
                        self.navigationController?.pushViewController(profileVC, animated: true)
                    }
                }
                else{
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }

            })
            
        }
        cell.onClickAddBuddy = {() -> Void in
            DispatchQueue.main.async {
                let inviteVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "InviteBuddiesViewController") as! InviteBuddiesViewController
                inviteVC.strTripId = objTrip.event_id
                inviteVC.value = 1
                self.navigationController?.pushViewController(inviteVC, animated: true)
            }

        }
        
        cell.lblName.layer.cornerRadius = cell.lblName.frame.size.width/2
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        self.prototypeCell = tblList.dequeueReusableCell(withIdentifier: "serachTrips_Cell")! as! SerachTripsTableViewCell
        
        let objVal = arrTripList[indexPath.row] as! TripsDetailsBO
        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblDesc.frame.size.width, font: prototypeCell.lblDesc.font, string: objVal.trip_desc)
        objVal.cellHgt = cellHgt + 25
        return 250 + cellHgt
    }
    
    // MARK: -
    // MARK: - UIScrollViewDelegates
    
    
    // MARK:-
    // MARK:- FSCalendarDelegate & FSCalendarDataSource
    
    func minimumDate(for calendar: FSCalendar) -> Date {
        let todayDate = calendar.today!
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy MMM dd EEE"
        let strDate = formatter.string(from: todayDate)
        let arrVal = strDate.components(separatedBy: " ")
        lblYear.text = arrVal[0]
        lblDate.text = arrVal[3] + ", " + arrVal[2] + " " + arrVal[1]
        return calendar.today!
    }

    
    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        print("change page to \(self.dateFormatter.string(from: calendar.currentPage))")
    }
    
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        print("calendar did select date \(self.dateFormatter.string(from: date))")
//        lblDate.text = self.dateFormatter.string(from: date)
//        lblDate.textColor = .black
        if monthPosition == .previous || monthPosition == .next {
            calendar.setCurrentPage(date, animated: true)
        }
    }
    
    func calendar(_ calendar: FSCalendar, boundingRectWillChange bounds: CGRect, animated: Bool) {
        self.calanderConstraint.constant = bounds.height
        self.view.layoutIfNeeded()
    }
    
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        let center = CLLocationCoordinate2D(latitude: APP_DELEGATE.currLoc.coordinate.latitude, longitude: APP_DELEGATE.currLoc.coordinate.longitude)
        let northEast = CLLocationCoordinate2D(latitude: center.latitude + 0.001,
                                               longitude: center.longitude + 0.001)
        let southWest = CLLocationCoordinate2D(latitude: center.latitude - 0.001,
                                               longitude: center.longitude - 0.001)
        let viewport = GMSCoordinateBounds(coordinate: northEast, coordinate: southWest)
        let config = GMSPlacePickerConfig(viewport: viewport)
        let placePicker = GMSPlacePickerViewController(config: config)
        placePicker.delegate = self
        present(placePicker, animated: true, completion: nil)
    }
    
    
    // MARK: -
    // MARK: - GMSPlacePickerViewControllerDelegate Methods
    func placePicker(_ viewController: GMSPlacePickerViewController, didPick place: GMSPlace) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("Place name \(place.name)")
        print("Place address \(place.formattedAddress ?? "")")
        print("Place attributions \(String(describing: place.attributions))")
        print("Coordinates::\(place.coordinate.latitude)")
        placeCoord = place.coordinate
        txtLocation.text = " "
        txtLocation.placeholder = nil
        txtLocation.leftView = lengthyLabel
        txtLocation.leftViewMode = .always
        lengthyLabel?.text = place.formattedAddress
        if place.formattedAddress != nil {
            strAddress = place.formattedAddress!
        }
        else {
            Common.getAddressFromCurrentLatLong { (strAddresReturned) in
                self.strAddress = strAddresReturned
                self.lengthyLabel?.text = strAddresReturned
            }
        }
        selectedIndex = 2
        self.callTripList()
        
        //        self.callCharterListVC(strLat: String(latitude) as NSString, strLong: String(longitude) as NSString, strRadius: "50", valueFrom: 1)
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }
    
    func callTripList()
    {
        let dictVal = NSMutableDictionary()
        switch selectedIndex {
        case 0:
            dictVal["category"] = strCategory
            dictVal["dt1"]      = lblFromDate.text == "From Date" ? "" : lblFromDate.text
            dictVal["dt2"]      = lblToDate.text == "To Date" ? "" : lblToDate.text
        case 1:
            dictVal["user_lat"] = String(APP_DELEGATE.currLoc.coordinate.latitude) as NSString
            dictVal["radius"] = String(viewSlider.value)
            dictVal["user_long"] = String(APP_DELEGATE.currLoc.coordinate.longitude) as NSString
            dictVal["dt1"]      = lblFromDate.text == "From Date" ? "" : lblFromDate.text
            dictVal["dt2"]      = lblToDate.text == "To Date" ? "" : lblToDate.text
        case 2:
            let latitude = Double((placeCoord?.latitude)!)
            let longitude = Double((placeCoord?.longitude)!)
            dictVal["user_lat"] = String(latitude)
            dictVal["radius"] = "50"
            dictVal["user_long"] = String(longitude)
            dictVal["dt1"]      = lblFromDate.text == "From Date" ? "" : lblFromDate.text
            dictVal["dt2"]      = lblToDate.text == "To Date" ? "" : lblToDate.text
            
        default:
            break
        }
        TripsParser.callAllTripsService(dictVal, complete: { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    if arrList.count == 0
                    {
                        self.arrTripList.removeAllObjects()
                        self.tblList.reloadData()
                        self.tblList.isHidden = true
                        self.lblNoData.isHidden = false
                    }
                    else
                    {
                        self.arrTripList.removeAllObjects()
                        self.arrTripList = arrList as! NSMutableArray
                        self.tblList.reloadData()
                        self.tblList.scrollsToTop = true
                        self.tblList.isHidden = false
                        self.lblNoData.isHidden = true
                        
                    }
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.arrTripList.removeAllObjects()
                    self.tblList.reloadData()
                    self.tblList.isHidden = true
                    self.lblNoData.isHidden = false
                }
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
