//
//  AddCoursesViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/30/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel

class AddCoursesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate , UITextViewDelegate, GMSPlacePickerViewControllerDelegate{
    
    @IBOutlet weak var viewUpdate: UIView!
    @IBOutlet weak var btnSub: UIButton!
    @IBOutlet weak var viewOtherHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtOther: UITextField!
    @IBOutlet weak var contentVHghtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewOther: UIView!
    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var txtPrice: UITextField!
    @IBOutlet weak var txtDesc: UITextView!
    @IBOutlet weak var txtDuration: UITextField!
    @IBOutlet weak var txtWebsite: UITextField!
    @IBOutlet weak var txtAgencyName: UITextField!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var btnSelectCourse: UIButton!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!

    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblCourse: UILabel!
    @IBOutlet weak var lblBadgeCount: UILabel!
    var strCourseType = String()
    var lengthyLabel: MarqueeLabel?
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var coursePic = UIImage()
    var indexVal = Int()
    var objCourseDetails = CoursesDetailsBO()

    let arrNames = ["Freediving", "Scuba Diving", "Others"]
    override func viewDidLoad() {
        super.viewDidLoad()

        let myColor = UIColor.lightGray
        tblViewList.layer.borderColor = myColor.cgColor
        tblViewList.layer.borderWidth = 1.0
        txtDesc.layer.borderColor = myColor.cgColor
        txtDesc.layer.borderWidth = 1.0
        btnSelectCourse.contentHorizontalAlignment = .right
        btnNext.layer.cornerRadius = 5.0
        contentVHghtConstraint.constant = btnNext.frame.maxY + 60
        
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        
        txtPrice.leftViewMode = UITextFieldViewMode.always
        let lblFullPrice = UILabel(frame: CGRect(x: 0, y: 0, width: 30, height: txtPrice.frame.size.height))
        lblFullPrice.text = "$  "
        lblFullPrice.textColor = .darkGray
        lblFullPrice.font = txtPrice.font
        txtPrice.leftView = lblFullPrice
        
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        viewOther.isHidden = true
        strCourseType = ""
        
        if !objCourseDetails.user_id.isEmpty {
            txtPrice.text             = objCourseDetails.price
            txtAgencyName.text          = objCourseDetails.agency_name
            txtDesc.textColor           = .black
            txtDesc.text                = objCourseDetails.course_desc
            txtDuration.text            = objCourseDetails.duration
            txtLocation.leftView        = lengthyLabel
            txtLocation.leftViewMode    = .always
            lengthyLabel?.text          = objCourseDetails.user_loc
            strAddress                  = objCourseDetails.user_loc
            lblCourse.text              = objCourseDetails.category
            if !objCourseDetails.agency_url.isEmpty
            {
                txtWebsite.text = objCourseDetails.agency_url
            }
            strCourseType = objCourseDetails.category
            let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtDesc.frame.width, font: txtDesc.font!, string: objCourseDetails.course_desc) + 25
            txtDescHgtConstraint.constant  = valHgt
            contentVHghtConstraint.constant = btnNext.frame.maxY + 60 + valHgt
            viewUpdate.isHidden = false
            btnNext.isHidden = true
            lblCourse.textColor = .black
            if objCourseDetails.category.contains("Others:")
            {
                let arrval = objCourseDetails.category.components(separatedBy: ":")
                txtOther.text = arrval.last
                lblCourse.text = arrval.first
                viewOtherHgtConstraint.constant = 40
                viewOther.isHidden = false
            }
            else
            {
                lblCourse.text = objCourseDetails.category
            }
        }
        else{
            viewUpdate.isHidden = true
            btnNext.isHidden = false
            contentVHghtConstraint.constant = btnNext.frame.maxY + 60
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
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnSub(_ sender: UIButton) {
        viewOtherHgtConstraint.constant = 0
        viewOther.isHidden = true
        lblCourse.text = "Select course type"
        lblCourse.textColor = .lightGray
        strCourseType = ""
    }

    @IBAction func onClickBtnSelectCourse(_ sender: UIButton) {
        UIView.animate(withDuration: 0.4, animations: {
            let higntcons = 90
            self.contentVHghtConstraint.constant = self.btnNext.frame.maxY + 150
            self.tblViewHgtConstraint.constant = CGFloat(higntcons)
        }, completion: { (completed) in
            
        })
    }
    
    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        
        let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this charter?", preferredStyle: UIAlertControllerStyle.alert)
        let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            let dictVal = NSMutableDictionary()
            dictVal["type"] = "course"
            dictVal["data_id"] = self.objCourseDetails.course_id
            TripsParser.callDeleteService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                var isFound:Bool = false
                                if let viewControllers = self.navigationController?.viewControllers {
                                    for viewController in viewControllers {
                                        // some process
                                        if viewController.isKind(of: self.indexVal == 1 ? CourseListViewController.self : MyListingViewController.self) {
                                            isFound = true
                                            if self.indexVal == 1 {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateCourseList"), object: nil)
                                            }
                                            else if self.indexVal == 2
                                            {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateMyList"), object: nil)
                                            }
                                            else{
                                                
                                            }
                                            self.navigationController?.popToViewController(viewController, animated: true)
                                        }
                                    }
                                    if !isFound
                                    {
                                        self.navigationController?.popViewController(animated: true)
                                    }
                                }
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
        let cancelAction = UIAlertAction(title: NSLocalizedString("NO", comment: "No action"), style: .cancel, handler: nil)
        alert.addAction(okAction)
        alert.addAction(cancelAction)
        self.present(alert, animated: true, completion: nil)

    }
    @IBAction func onClickBtnUpdate(_ sender: UIButton) {
        self.view.endEditing(true)
        if strCourseType.isEmpty {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else if strCourseType == "Others: " && (txtOther.text?.isEmpty)!
        {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (txtAgencyName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your agency name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDuration.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your course duration", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDesc.text == "Enter course description")
        {
            Common.showAlert(message: "Please enter your course description", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtPrice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your course price", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else{
            
            let dictVal = NSMutableDictionary()
            dictVal["category"]         = strCourseType == "Others: " ? "Others: " + txtOther.text! : strCourseType
            dictVal["agency_nm"]        = txtAgencyName.text
            dictVal["co_description"]   = txtDesc.text
            dictVal["co_location"]      = strAddress
            dictVal["agency_url"]       = txtWebsite.text
            dictVal["co_price"]         = txtPrice.text
            dictVal["co_duration"]      = txtDuration.text
            if (placeCoord != nil) {
                let latitude: Double = Double((placeCoord?.latitude)!)
                let longitude: Double = Double((placeCoord?.longitude)!)
                dictVal["co_lat"]           = String(latitude)
                dictVal["co_long"]          = String(longitude)
            }
            else
            {
                dictVal["co_lat"]           = ""
                dictVal["co_long"]          = ""
            }
            dictVal["co_id"]            = objCourseDetails.course_id
            
            MarketParser.callUpdateCourseService(dictVal, _isFromPic: false, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                self.callCourseDetailsService(self.objCourseDetails.course_id as NSString)
                            }
                            
                            })
                            alert.addAction(okAction)
                            self.present(alert, animated: true, completion: nil)
                    }
                }
            })
        }

    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if strCourseType.isEmpty {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else if strCourseType == "Others: " && (txtOther.text?.isEmpty)!
        {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }

        else if (txtAgencyName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your agency name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDuration.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your course duration", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDesc.text == "Enter course description")
        {
            Common.showAlert(message: "Please enter your course description", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtPrice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your course price", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let latitude: Double = Double((placeCoord?.latitude)!)
            let longitude: Double = Double((placeCoord?.longitude)!)
            let dictVal = NSMutableDictionary()
            dictVal["category"]         = strCourseType == "Others: " ? "Others: " + txtOther.text! : strCourseType
            dictVal["agency_nm"]        = txtAgencyName.text
            dictVal["co_description"]   = txtDesc.text
            dictVal["co_location"]      = strAddress
            dictVal["agency_url"]       = txtWebsite.text
            dictVal["co_price"]         = txtPrice.text
            dictVal["co_duration"]      = txtDuration.text
            dictVal["co_lat"]           = String(latitude)
            dictVal["co_long"]          = String(longitude)
            dictVal["co_img"]           = coursePic
            MarketParser.callCreateCourseService(dictVal, complete: { (status, strMsg, strId) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                self.callCourseDetailsService(strId as NSString)
                              }
                        })
                        alert.addAction(okAction)
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
//        let listVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FeatureListViewController") as! FeatureListViewController
//        self.navigationController?.pushViewController(listVC, animated: true)
        
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
        lblCourse.text = arrNames[indexPath.row]
        lblCourse.textColor = .black
//        strCourseType = arrNames[indexPath.row].lowercased()
        strCourseType = arrNames[indexPath.row]
        UIView.animate(withDuration: 0.4, animations: {
            self.contentVHghtConstraint.constant -= 90
            self.tblViewHgtConstraint.constant = 0
        }, completion: { (completed) in
        })
        
        if arrNames[indexPath.row] == "Others" {
            strCourseType = "Others: "
            viewOtherHgtConstraint.constant = 40
            viewOther.isHidden = false
        }
        else{
            viewOtherHgtConstraint.constant = 0
            viewOther.isHidden = true
        }
    }
    
    // MARK: -
    // MARK: - UITextView Delegates
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtDesc.text == "Enter course description"
        {
            txtDesc.text = ""
            txtDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Enter course description"
            txtDesc.textColor = .lightGray
            
        }
    }
    func textViewDidChange(_ textView: UITextView) {
        
        self.textViewDynamicallyIncreaseSize()
        
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
    
    // MARK: -
    // MARK: - Other Methods
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtDesc.sizeThatFits(self.txtDesc.bounds.size)
        let higntcons = contentSize.height
        txtDescHgtConstraint.constant = higntcons
        contentVHghtConstraint.constant = btnNext.frame.maxY + 60
        
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Enter course description"
            txtDesc.textColor = .lightGray
        }
        self.view.endEditing(true)
    }

    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        if textField == txtLocation
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
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }

    
    func callCourseDetailsService( _ strId: NSString)
    {
        MarketParser.callCourseDetailsService(["id" : strId], complete: { (status, strMsg, objDetails) in
            if status
            {
                DispatchQueue.main.async {
                    let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CourseDetailsViewController") as! CourseDetailsViewController
                    detailsVC.objDetails = objDetails
                    detailsVC.indexVal = self.indexVal
                    self.navigationController?.pushViewController(detailsVC, animated: true)
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
