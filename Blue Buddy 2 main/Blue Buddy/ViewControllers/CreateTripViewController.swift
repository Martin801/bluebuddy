//
//  CreateTripViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/22/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel
class CreateTripViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate, FSCalendarDataSource, FSCalendarDelegate, UIGestureRecognizerDelegate, UITextViewDelegate {

    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var contentVHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnSub: UIButton!
    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var viewUpdate: UIView!
    @IBOutlet weak var viewOtherActivirty: UIView!
    @IBOutlet weak var calanderConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewCalender: FSCalendar!
    var lengthyLabel: MarqueeLabel?
    var strAddress = String()
    var strDate = String()
    var strActivity = String()
    var isFromUpdate = Bool()
    var indexval = Int()
    var objTrips = TripsDetailsBO()
    var placeCoord: CLLocationCoordinate2D?
    @IBOutlet weak var viewActivityHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtOtherActivity: UITextField!
    @IBOutlet weak var lblTodayDate: UILabel!
    @IBOutlet weak var lblYear: UILabel!
    @IBOutlet weak var viewDatePopUp: UIView!
    @IBOutlet weak var lblActivity: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtDes: UITextView!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var btnDate: UIButton!
    @IBOutlet weak var btnSelectActivity: UIButton!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblBadgeCnt: UILabel!
    let arrNames = ["Freediving", "Scuba Diving", "Spearfishing", "Photography", "Fishing", "Snorkeling", "Kayaking", "Trolling", "Others"]
//    fileprivate lazy var dateFormatter: DateFormatter = {
//        let formatter = DateFormatter()
//        formatter.dateFormat = "yyyy/MM/dd"
//        return formatter
//    }()

    
    var onClickDelete: (() -> Void)? = nil
    var onClickEdit: ((TripsDetailsBO) -> Void)? = nil
    override func viewDidLoad() {
        super.viewDidLoad()

        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.black
        lengthyLabel?.type = .continuous
        let myColor = UIColor.lightGray
        tblViewList.layer.borderColor = myColor.cgColor
        tblViewList.layer.borderWidth = 1.0
        txtDes.layer.borderColor = myColor.cgColor
        txtDes.layer.borderWidth = 1.0
        btnSelectActivity.contentHorizontalAlignment = .right
        btnDate.contentHorizontalAlignment = .right
        viewDatePopUp.isHidden = true
        viewCalender.appearance.headerMinimumDissolvedAlpha = 0;
        viewCalender.appearance.caseOptions = .weekdayUsesSingleUpperCase;
        viewCalender.firstWeekday = 2;
        viewOtherActivirty.isHidden = true
        lblBadgeCnt.layer.cornerRadius = lblBadgeCnt.frame.size.height/2
        lblBadgeCnt.clipsToBounds = true
        self.viewDatePopUp.frame = CGRect(x: 0, y: 0, width: (APP_DELEGATE.window?.frame.size.width)!, height: (APP_DELEGATE.window?.frame.size.height)!)
        UIApplication.shared.keyWindow!.addSubview(self.viewDatePopUp)
        viewDatePopUp.translatesAutoresizingMaskIntoConstraints = true

        if isFromUpdate {
            lblHeader.text = "EDIT YOUR TRIP"
            txtLocation.text = " "
            txtLocation.placeholder = nil
            txtLocation.leftView = lengthyLabel
            txtLocation.leftViewMode = .always
            lengthyLabel?.text = objTrips.event_loc
            txtDes.text = objTrips.trip_desc
            
            let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtDes.frame.width, font: txtDes.font!, string: objTrips.trip_desc) + 25
            txtDescHgtConstraint.constant = valHgt
            contentVHgtConstraint.constant = btnSubmit.frame.maxY + 60 + valHgt
            strAddress = objTrips.event_loc
            strDate = APP_DELEGATE.changeOneFormateToOther("yyyy-MM-dd", "dd.MM.yyyy", objTrips.event_date1)
            strActivity = objTrips.activity
            
            lblActivity.textColor = .black
            if objTrips.activity.contains("Others:")
            {
                let arrval = objTrips.activity.components(separatedBy: ":")
                txtOtherActivity.text = arrval.last
                lblActivity.text = arrval.first
                viewActivityHgtConstraint.constant = 40
                viewOtherActivirty.isHidden = false
            }
            else
            {
                lblActivity.text = objTrips.activity
            }

            lblDate.textColor = .black
            txtDes.textColor = .black
            lblDate.text = objTrips.event_date
            btnSubmit.isHidden = true
            viewUpdate.isHidden = false
        }
        else{
            lblHeader.text = "CREATE YOUR TRIP"
            btnSubmit.isHidden = false
            viewUpdate.isHidden = true
            contentVHgtConstraint.constant = btnSubmit.frame.maxY + 70
        }
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblBadgeCnt.text = strCounter
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
    
    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnDateEvent(_ sender: UIButton) {
        if sender.tag == 1
        {
            lblDate.text = "Please enter your event date"
            lblDate.textColor = .lightGray
            strDate = ""
        }
        viewDatePopUp.isHidden = true
    }
    @IBAction func onClickBtnDate(_ sender: UIButton) {
        viewDatePopUp.isHidden = false
    }

    @IBAction func onClickBtnSub(_ sender: UIButton) {
        
        viewActivityHgtConstraint.constant = 0
        viewOtherActivirty.isHidden = true
        lblActivity.text = "Select activity type"
        lblActivity.textColor = .lightGray
        strActivity = ""
    }
    @IBAction func onClickBtnActivity(_ sender: UIButton) {
        UIView.animate(withDuration: 0.4, animations: {
            let higntcons = 180
            self.tblViewHgtConstraint.constant = CGFloat(higntcons)
        }, completion: { (completed) in
            
        })
    }
    
    @IBAction func onClickBtnUpdate(_ sender: UIButton) {
        self.view.endEditing(true)
        if strAddress.isEmpty {
            Common.showAlert(message: "Please enter your trip location", title: "BLUE BUDDY", viewController: self)
        }
        else if strDate.isEmpty
        {
            Common.showAlert(message: "Please enter your trip date", title: "BLUE BUDDY", viewController: self)
        }
            
        else if strActivity.isEmpty {
            Common.showAlert(message: "Please enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else if txtDes.text == "Please enter your trip details(Max 250 character)"
        {
            Common.showAlert(message: "Please enter your trip description", title: "BLUE BUDDY", viewController: self)
        }
        else if strActivity == "Others: " && (txtOtherActivity.text?.isEmpty)!
        {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else{
           
            
            let dictVal = NSMutableDictionary()
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            if ((placeCoord?.latitude) != nil) {
                latitude = Double((placeCoord?.latitude)!)
                longitude = Double((placeCoord?.longitude)!)
                

            }
            dictVal["trip_location"] = strAddress
            dictVal["trip_id"] = objTrips.event_id
            dictVal["trip_dt"] = strDate
            dictVal["trip_type"] = strActivity == "Others: " ? "Others: " + txtOtherActivity.text! : strActivity
            dictVal["description"] = txtDes.text
            dictVal["trip_lat"] = latitude == 0 ? "" : String(latitude)
            dictVal["trip_long"] = longitude == 0 ? "" : String(longitude)
            TripsParser.callUpdateTripService(dictVal, complete: { (status, strMsg) in
                if status{
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                let objVal: TripsDetailsBO = self.objTrips
                                objVal.event_loc = self.strAddress
                                objVal.activity = self.strActivity == "Others: " ? "Others: " + self.txtOtherActivity.text! : self.strActivity.capitalized
                                objVal.trip_desc = self.txtDes.text
                                objVal.event_date = self.lblDate.text!
                                if ((self.onClickEdit) != nil) {
                                    self.onClickEdit!(objVal)
                                }
                                self.navigationController?.popViewController(animated: true)

                            }
                        })
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)


                    }
                    
                }
                else{
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            })
        }
        
    }
    
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this trip?", preferredStyle: UIAlertControllerStyle.alert)
        let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            let dictVal = NSMutableDictionary()
            dictVal["type"] = "trip"
            dictVal["data_id"] = self.objTrips.event_id
            TripsParser.callDeleteService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                               
                                if ((self.onClickDelete) != nil) {
                                    self.onClickDelete!()
                                }
                                self.navigationController?.popViewController(animated: true)
                            }
                        })
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
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
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if strAddress.isEmpty {
            Common.showAlert(message: "Please enter your trip location", title: "BLUE BUDDY", viewController: self)
        }
        else if strDate.isEmpty
        {
            Common.showAlert(message: "Please enter your trip date", title: "BLUE BUDDY", viewController: self)
        }
            
        else if strActivity.isEmpty {
            Common.showAlert(message: "Please enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else if txtDes.text == "Please enter your trip details(Max 250 character)"
        {
            Common.showAlert(message: "Please enter your trip description", title: "BLUE BUDDY", viewController: self)
        }
        else if strActivity == "Others: " && (txtOtherActivity.text?.isEmpty)!
        {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else{
            
            let latitude: Double = Double((placeCoord?.latitude)!)
            let longitude: Double = Double((placeCoord?.longitude)!)
            let dictVal = NSMutableDictionary()
            dictVal["trip_lat"] = String(latitude)
            dictVal["trip_long"] = String(longitude)
            dictVal["trip_location"] = strAddress
            dictVal["trip_dt"] = strDate
            dictVal["trip_type"] = strActivity == "Others: " ? "Others: " + txtOtherActivity.text! : strActivity
            dictVal["description"] = txtDes.text
            TripsParser.callCreateTripsService(dictVal, complete: { (status, strMsg, objTripDetails) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: "Thanks for creating your Trips. You can now invite your buddies to join the trip.", preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("Invite Buddy", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                let inviteVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "InviteBuddiesViewController") as! InviteBuddiesViewController
                                inviteVC.strTripId = objTripDetails.event_id
                                inviteVC.value = self.indexval
                                self.navigationController?.pushViewController(inviteVC, animated: true)
                            }
                        })
                        let cancelAction = UIAlertAction(title: "Skip", style: .cancel, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "TripDetailsViewController") as! TripDetailsViewController
                                detailsVC.objTripDetail = objTripDetails
                                detailsVC.indexVal = self.indexval
                                self.navigationController?.pushViewController(detailsVC, animated: true)
                            }
                        })
                        alert.addAction(okAction)
                        alert.addAction(cancelAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                }
                else{
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            })
        }
    }

    @IBAction func onClickTapOutsidePopUp(_ sender: UITapGestureRecognizer) {
        viewDatePopUp.isHidden = true

    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrNames.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "simpleCell", for: indexPath)
        cell.textLabel?.text = arrNames[indexPath.row]
        cell.textLabel?.textColor = .darkGray
        cell.textLabel?.font = UIFont.systemFont(ofSize: 15.0)
        
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        lblActivity.text = arrNames[indexPath.row]
        lblActivity.textColor = .black
        strActivity = arrNames[indexPath.row].capitalized
        UIView.animate(withDuration: 0.4, animations: {
            self.tblViewHgtConstraint.constant = 0
        }, completion: { (completed) in
            
        })
        if arrNames[indexPath.row] == "Others" {
            strActivity = "Others: "
            viewActivityHgtConstraint.constant = 40
            viewOtherActivirty.isHidden = false
        }
        else
        {
            viewActivityHgtConstraint.constant = 0
            viewOtherActivirty.isHidden = true
        }
    }

    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        self.view.endEditing(true)
        let center = CLLocationCoordinate2D(latitude: APP_DELEGATE.currLoc.coordinate.latitude, longitude: APP_DELEGATE.currLoc.coordinate.longitude)
        
        let locationManager = CLLocationManager()
        // Ask for Authorisation from the User.
       locationManager.requestAlwaysAuthorization()
        
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
        txtLocation.text = " "
        txtLocation.placeholder = nil
        txtLocation.leftView = lengthyLabel
        txtLocation.leftViewMode = .always
        lengthyLabel?.text = place.formattedAddress
        placeCoord = place.coordinate
        if place.formattedAddress != nil {
            strAddress = place.formattedAddress!
        }
        else {
            self.getAddressFromCurrentLatLong()
        }
        
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }
    
    
    
    func getAddressFromCurrentLatLong() {
        
        let objLocation = CLLocation(latitude: APP_DELEGATE.currLoc.coordinate.latitude, longitude: APP_DELEGATE.currLoc.coordinate.longitude)
        CLGeocoder().reverseGeocodeLocation(objLocation) { (placemarksArray, error) in
            if error != nil {
                print("Reverse geocoder failed with error" + (error?.localizedDescription)!)
                self.strAddress = ""
                return
            }
            
            if (placemarksArray?.count)! > 0 {
                let objPlacemark = placemarksArray?[0]
                
                print("objPlacemark : \(objPlacemark?.description)")
                self.strAddress = (objPlacemark?.name)! as String
                self.lengthyLabel?.text = self.strAddress
            }
            else {
                self.strAddress = ""
                print("Problem with the data received from geocoder")
            }
        }
    }
    
    // MARK:- FSCalendarDataSource
    
//    func calendar(_ calendar: FSCalendar, titleFor date: Date) -> String? {
//        return self.gregorian.isDateInToday(date) ? "今天" : nil
//    }
    
    
    func minimumDate(for calendar: FSCalendar) -> Date {
        let strDate = APP_DELEGATE.changeDateToString("yyyy MMM dd EEE", calendar.today!) as String
        let arrVal = strDate.components(separatedBy: " ")
        lblYear.text = arrVal[0]
        lblTodayDate.text = arrVal[3] + ", " + arrVal[2] + " " + arrVal[1]
        return calendar.today!
    }
    
    
    // MARK:- FSCalendarDelegate
    
    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        print("change page to \(APP_DELEGATE.changeDateToString("yyyy/MM/dd", calendar.currentPage))")
    }
    
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        print("change page to \(APP_DELEGATE.changeDateToString("yyyy/MM/dd", date))")
        
        lblDate.text = APP_DELEGATE.changeDateToString("dd MMMM, yyyy", date) as String
        lblDate.textColor = .black
        strDate = APP_DELEGATE.changeDateToString("dd.MM.yyyy", date) as String
        if monthPosition == .previous || monthPosition == .next {
            calendar.setCurrentPage(date, animated: true)
        }
    }
    
    func calendar(_ calendar: FSCalendar, boundingRectWillChange bounds: CGRect, animated: Bool) {
        self.calanderConstraint.constant = bounds.height
        self.view.layoutIfNeeded()
    }
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtDes.text == "Please enter your trip details (Max 250 character)"
        {
            txtDes.text = ""
            txtDes.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtDes.text.isEmpty
        {
            txtDes.text = "Please enter your trip details (Max 250 character)"
            txtDes.textColor = .lightGray
            
        }
    }
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        let length = (textView.text?.characters.count)! + text.characters.count - range.length
        if length < 251 {
            return true
        }
        else{
            return false
        }
    }
    
    func textViewDidChange(_ textView: UITextView) {
        
        self.textViewDynamicallyIncreaseSize()
        
    }
    
    // MARK: -
    // MARK: - Other Methods
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtDes.sizeThatFits(self.txtDes.bounds.size)
        let higntcons = contentSize.height < 85 ? 85 : contentSize.height
        txtDescHgtConstraint.constant = higntcons
        contentVHgtConstraint.constant = btnSubmit.frame.maxY + 70
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if txtDes.text.isEmpty
        {
            txtDes.text = "Please enter your trip details(Max 250 character)"
            txtDes.textColor = .lightGray
        }
        self.view.endEditing(true)
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
