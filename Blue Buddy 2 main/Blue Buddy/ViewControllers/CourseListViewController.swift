//
//  CourseListViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/30/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel
import NVActivityIndicatorView
class CourseListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , UITextFieldDelegate, GMSPlacePickerViewControllerDelegate{

    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtLoc: UITextField!
    @IBOutlet weak var lblSliderVal: UILabel!
    @IBOutlet weak var sliderView: UISlider!
    @IBOutlet weak var viewLocHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewOtherLoc: UIView!
    @IBOutlet weak var viewCurrentLoc: UIView!
    @IBOutlet weak var viewLoc: UIView!
    @IBOutlet weak var viewBtn: UIView!
    @IBOutlet weak var btnOtherLoc: UIButton!
    @IBOutlet weak var btnCurrentLoc: UIButton!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var lblBagdeNo: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    var arrCourseList = NSMutableArray()
    var arrCategoryList = NSMutableArray()
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var lengthyLabel: MarqueeLabel?
    var isCurrentLoc = Bool()
    var selectedIndex: Int = 0
    var tblconst = CGFloat()
    var timer = Timer()
    override func viewDidLoad() {
        super.viewDidLoad()

        let myColor = UIColor.darkGray
        btnOtherLoc.layer.borderColor         = myColor.cgColor
        btnOtherLoc.layer.borderWidth         = 1.0
        btnOtherLoc.layer.cornerRadius        = 5.0
        btnCurrentLoc.layer.borderColor             = myColor.cgColor
        btnCurrentLoc.layer.borderWidth             = 1.0
        btnCurrentLoc.layer.cornerRadius            = 5.0
        lengthyLabel = MarqueeLabel.init(frame: txtLoc.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        lblBagdeNo.layer.cornerRadius = lblBagdeNo.frame.size.height/2
        lblBagdeNo.clipsToBounds = true
        viewLoc.isHidden = true
        lblNoData.isHidden = true
        viewLocHgtConstraint.constant = 0
        self.callCourseListWebservice()
        tblViewHgtConstraint.constant = view.frame.size.height - (viewBtn.frame.maxY + 65)
        NotificationCenter.default.addObserver(self, selector: #selector(CourseListViewController.callCourseListWebservice), name: NSNotification.Name(rawValue: "updateCourseList"), object: nil)
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblBagdeNo.text = strCounter
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
    @IBAction func onClickBtnHeaderSelection(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        if sender.isSelected
        {
            tblViewHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY + 65)
            switch sender.tag {
            case 1:
                selectedIndex = 1
//                self.callCharterListVC(strLat: "22.5726", strLong: "88.3639", strRadius: String(sliderView.value) as NSString, valueFrom: 1)
                viewLoc.isHidden = false
                self.btnCurrentLoc.setTitleColor(.white, for: .selected)
                self.btnCurrentLoc.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnOtherLoc.setTitleColor(.black, for: .normal)
                sliderView.value = 1
                btnOtherLoc.isSelected = false
                lblSliderVal.text = "1" + "km"
                self.btnOtherLoc.backgroundColor = .white
                self.viewCurrentLoc.isHidden = false
                self.viewOtherLoc.isHidden = true
                viewLocHgtConstraint.constant = 65
                self.callCourseListWebservice()

//                self.tblViewList.frame = CGRect(x: (self.view.frame.size.width - self.tblViewList.frame.size.width)/2, y: self.tblViewList.frame.origin.y, width: self.tblViewList.frame.size.width, height: self.view.frame.size.height - (self.viewCurrentLoc.frame.maxY + 65))
                self.view.bringSubview(toFront: self.viewCurrentLoc)

            case 2:
                selectedIndex = 2
                viewLoc.isHidden = false
                self.btnCurrentLoc.titleLabel?.textColor = .black
                self.btnOtherLoc.setTitleColor(.white, for: .selected)
                self.btnOtherLoc.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnCurrentLoc.setTitleColor(.black, for: .normal)
                self.btnCurrentLoc.backgroundColor = .white
                self.btnCurrentLoc.isSelected = false
                self.viewOtherLoc.isHidden = false
                self.viewCurrentLoc.isHidden = true
                viewLocHgtConstraint.constant = 65
                self.view.bringSubview(toFront: self.viewOtherLoc)
                
            default:
                selectedIndex = 0
                tblViewHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY)
                viewLoc.isHidden = true
                self.btnCurrentLoc.isSelected = false
                self.btnOtherLoc.isSelected = false
                self.btnOtherLoc.setTitleColor(.black, for: .normal)
                self.btnOtherLoc.backgroundColor = .white
                self.btnCurrentLoc.setTitleColor(.black, for: .normal)
                self.btnCurrentLoc.backgroundColor = .white
                self.viewOtherLoc.isHidden = true
                self.viewCurrentLoc.isHidden = true
                viewLocHgtConstraint.constant = 0
                self.callCourseListWebservice()
                break
            }
        }
        else
        {
            tblViewHgtConstraint.constant = view.frame.size.height - viewLoc.frame.maxY
            selectedIndex = 0
            viewLoc.isHidden = true
            self.btnCurrentLoc.isSelected = false
            self.btnOtherLoc.isSelected = false
            self.btnOtherLoc.setTitleColor(.black, for: .normal)
            self.btnOtherLoc.backgroundColor = .white
            self.btnCurrentLoc.setTitleColor(.black, for: .normal)
            self.btnCurrentLoc.backgroundColor = .white
            self.viewOtherLoc.isHidden = true
            self.viewCurrentLoc.isHidden = true
            viewLocHgtConstraint.constant = 0
            self.callCourseListWebservice()
        }
    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onChangeSliderValue(_ sender: UISlider) {
        let value = Int (sender.value)
        lblSliderVal.text = String (value) + " km"
        selectedIndex = 1
        NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(self.callCourseListWebservice), object: nil)
        if (timer.isValid) {
            timer.invalidate()
        }
        timer = Timer(timeInterval: 0.9, target: self, selector: #selector(self.callCourseListWebservice), userInfo: nil, repeats: false)
        RunLoop.main.add(timer, forMode: RunLoopMode.defaultRunLoopMode)
    }
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        
        DispatchQueue.main.async {
            let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
            uploadVC.index = 2
            uploadVC.value = 1
            self.navigationController?.pushViewController(uploadVC, animated: true)
        }
    }
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    func callChatDetails( _ strId: String, _ targetVc: UIViewController) {
        let activityData = ActivityData()
        NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
            NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
        }
        User.info(forUserID: strId, completion: { (user) in
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
            }
            DispatchQueue.main.async {
                let chatVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                chatVC.objUser = user
                targetVc.present(chatVC, animated: true, completion: nil)
            }
        })
    }

    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrCourseList.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var firebase_id = String()
        let cell = tableView.dequeueReusableCell(withIdentifier: "boatRental_Cell", for: indexPath) as! BoatRentalTableViewCell
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
        cell.lblPrice.layer.cornerRadius = cell.lblPrice.frame.size.height/2
        cell.lblPrice.clipsToBounds = true
        cell.lblName.layer.cornerRadius = cell.lblName.frame.size.width/2
        let objValue = arrCourseList[indexPath.row] as!CoursesDetailsBO
        cell.lblName.text = objValue.category
        cell.lblPrice.text = "$" + objValue.price
        cell.lblAddress.text = objValue.user_loc
        cell.lblPostedBy.text = objValue.user_name
        cell.imgProduct.layer.cornerRadius = 5.0
        cell.imgProduct.clipsToBounds = true
        cell.imgFeatured.isHidden = objValue.is_featured ? false : true
        cell.imgProduct.kf.setImage(with: URL(string:objValue.course_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        if objValue.is_editable
        {
            cell.btnChat.isHidden = false
            cell.btnChat.setTitle("EDIT", for: .normal)
            cell.btnChat.isEnabled = true
            cell.imgFlag.isHidden = true
            cell.btnFlag.isHidden = true
        }
        else
        {
            cell.btnChat.isHidden = false
            cell.btnChat.isEnabled = true
            cell.btnChat.setTitle("CHAT", for: .normal)
            cell.btnChat.setBackgroundImage(#imageLiteral(resourceName: "Rectangle 90.png"), for: .normal)
            cell.imgFlag.isHidden = false
            cell.btnFlag.isHidden = false
            cell.imgFlag.image = objValue.is_flagged ? #imageLiteral(resourceName: "report_flag.png") : #imageLiteral(resourceName: "flag.png")
        }

        firebase_id = objValue.firebase_id
        cell.onClickDetails = {() -> Void in
            self.callDetailsfrom(false, objValue.course_id)
        }
        cell.onClickOtherBtn = {() -> Void in
//            self.callDetailsfrom(true, objValue.course_id)
//            self.callChatDetails(objValue.firebase_id, self)
            
            if objValue.is_editable
            {
                self.callDetailsfrom(true, objValue.course_id)
            }
            else
            {
                ProfileViewController.callChatDetails(objValue.firebase_id, self)
            }
            
        }
        cell.onClickReport = {() -> Void in
            
            let alert = UIAlertController(title: "BLUE BUDDY", message: "Are you sure you want to report?", preferredStyle: UIAlertControllerStyle.alert)
            let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                let dictVal = NSMutableDictionary()
                dictVal["type"] = "course"
                dictVal["type_id"] = objValue.course_id
                TripsParser.callReportService(dictVal, complete: { (status, strMsg) in
                    if status
                    {
                        cell.imgFlag.image = #imageLiteral(resourceName: "report_flag.png")
                        objValue.is_flagged = true
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

        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 237
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
        txtLoc.text = " "
        txtLoc.placeholder = nil
        txtLoc.leftView = lengthyLabel
        txtLoc.leftViewMode = .always
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
        self.callCourseListWebservice()

//        self.callCharterListVC(strLat: String(latitude) as NSString, strLong: String(longitude) as NSString, strRadius: "50", valueFrom: 1)
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }
    
    func callCourseListWebservice()
    {
        let dictVal = NSMutableDictionary()
        switch selectedIndex {
        case 0:
            dictVal["category"] = arrCategoryList.componentsJoined(by: ",")
        case 1:
            dictVal["user_lat"] = String(APP_DELEGATE.currLoc.coordinate.latitude) as NSString
            dictVal["radius"] = String(sliderView.value)
            dictVal["user_long"] = String(APP_DELEGATE.currLoc.coordinate.longitude) as NSString
        case 2:
            let latitude = Double((placeCoord?.latitude)!)
            let longitude = Double((placeCoord?.longitude)!)
            dictVal["user_lat"] = String(latitude)
            dictVal["radius"] = "50"
            dictVal["user_long"] = String(longitude)

        default:
            break
        }
        MarketParser.callCourseListService(dictVal, complete: { (status, strMsg, arrValue) in
            if status
            {
                DispatchQueue.main.async {
                    self.arrCourseList.removeAllObjects()
                    self.arrCourseList = arrValue as! NSMutableArray
                    self.tblViewList.reloadData()
                    self.tblViewList.isHidden = false
                    self.lblNoData.isHidden = true
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.arrCourseList.removeAllObjects()
                    self.tblViewList.reloadData()
                    self.tblViewList.isHidden = true
                    self.lblNoData.isHidden = false
                }
            }
        })
    }

    func callDetailsfrom( _ isFromEdit :Bool, _ strId: String)
    {
        MarketParser.callCourseDetailsService(["id" : strId], complete: { (status, strMsg, objCourseDetails) in
            if status
            {
                DispatchQueue.main.async {
                    if !isFromEdit
                    {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CourseDetailsViewController") as! CourseDetailsViewController
                        let objCourse = objCourseDetails
                        objCourse.course_id = strId
                        detailsVC.objDetails = objCourse
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                    else
                    {
                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                        uploadVC.index = 2
                        uploadVC.value = 1
                        let objCourse = objCourseDetails
                        objCourse.course_id = strId
                        uploadVC.courseDetails = objCourse
                        self.navigationController?.pushViewController(uploadVC, animated: true)
                    }
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
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
