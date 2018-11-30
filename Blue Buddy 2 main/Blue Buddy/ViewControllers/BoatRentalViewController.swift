//
//  BoatRentalViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/28/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel

class BoatRentalViewController: UIViewController,UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate {

  //  @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewLoc: UIView!
    @IBOutlet weak var viewBtn: UIView!
    @IBOutlet weak var viewLocHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblSlider: UILabel!
    @IBOutlet weak var sliderView: UISlider!
    @IBOutlet weak var viewCurrLoc: UIView!
    @IBOutlet weak var txtLoc: UITextField!
    @IBOutlet weak var viewOtherLoc: UIView!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var btnBoat: UIButton!
    @IBOutlet weak var btnLocation: UIButton!
    @IBOutlet weak var lblBagdeNo: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    var navigateFrom = Int ()
    var arrBoatList = NSMutableArray()
    var arrCategory = NSMutableArray()
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var lengthyLabel: MarqueeLabel?
    var isCurrentLoc = Bool()
    var timer = Timer()
    var selectedIndex:Int = 0
    override func viewDidLoad() {
        super.viewDidLoad()

        let myColor = UIColor.darkGray
        btnLocation.layer.borderColor         = myColor.cgColor
        btnLocation.layer.borderWidth         = 1.0
        btnLocation.layer.cornerRadius        = 5.0
        btnBoat.layer.borderColor             = myColor.cgColor
        btnBoat.layer.borderWidth             = 1.0
        btnBoat.layer.cornerRadius            = 5.0
        lblBagdeNo.layer.cornerRadius         = lblBagdeNo.frame.size.height/2
        lblBagdeNo.clipsToBounds              = true
        tblViewList.isHidden = true
        self.viewCurrLoc.isHidden = true
        self.viewOtherLoc.isHidden = true

        lengthyLabel = MarqueeLabel.init(frame: txtLoc.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        APP_DELEGATE.locationManager.startUpdatingLocation()
        if navigateFrom == 1
        {
            lblHeader.text = "PRODUCT LIST"
            NotificationCenter.default.addObserver(self,
                                                   selector: #selector(BoatRentalViewController.callProductListService),
                                                   name: NSNotification.Name(rawValue: "updateProductList"),
                                                   object: nil)
            self.callProductListService()
            
        }
        else
        {
            NotificationCenter.default.addObserver(self, selector: #selector(BoatRentalViewController.callCharterListVC), name: Notification.Name("updateCharterList"), object: nil)

//            NotificationCenter.default.addObserver(self,
//                                                   selector: #selector(BoatRentalViewController.callCharterListVC),
//                                                   name: NSNotification.Name(rawValue: "updateCharterList"),
//                                                   object: nil)
            self.callCharterListVC(strLat: "", strLong: "", strRadius: "", valueFrom: 1)

        }
       
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

       // tblViewHgtConstraint.constant = view.frame.size.height - (viewBtn.frame.maxY + 70)
        viewLoc.isHidden = true
        
        // Do any additional setup after loading the view.
    }

    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickBtnCurrLoc(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        
        switch sender.tag {
        case 1:
            if sender.isSelected
            {
                selectedIndex = 1
                sliderView.value = 1
                lblSlider.text = String(sliderView.value) + "km"
                if navigateFrom == 1
                {
                    self.callProductListService()
                }
                else
                {
                    self.callCharterListVC(strLat: String(APP_DELEGATE.currLoc.coordinate.latitude) as NSString, strLong: String(APP_DELEGATE.currLoc.coordinate.longitude) as NSString, strRadius: String(sliderView.value) as NSString, valueFrom: 1)
                }
               // tblViewHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY + 70)
                btnLocation.isSelected = false
                viewLoc.isHidden = false
                self.btnBoat.setTitleColor(.white, for: .selected)
                self.btnBoat.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnLocation.setTitleColor(.black, for: .normal)
                self.btnLocation.backgroundColor = .white
                self.viewCurrLoc.isHidden = false
                viewLocHgtConstraint.constant = 65
                self.view.bringSubview(toFront: self.viewCurrLoc)
                self.viewOtherLoc.isHidden = true
            }
            else{
                selectedIndex = 0
                btnBoat.isSelected = false
                btnLocation.isSelected = false
               // tblViewHgtConstraint.constant = view.frame.size.height - viewLoc.frame.maxY
                viewLoc.isHidden = true
                self.btnBoat.setTitleColor(.black, for: .normal)
                self.btnBoat.backgroundColor = .white
                self.btnLocation.setTitleColor(.black, for: .normal)
                self.btnLocation.backgroundColor = .white
                self.viewCurrLoc.isHidden = true
                self.viewOtherLoc.isHidden = true
                viewLocHgtConstraint.constant = 0
                if navigateFrom == 1
                {
                    self.callProductListService()
                }
                else
                {
                    self.callCharterListVC(strLat: "", strLong: "", strRadius: "", valueFrom: 1)
                }

                
            }
            case 2:
                if sender.isSelected
                {
                    selectedIndex = 2
                  //  tblViewHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY + 70)
                    viewLoc.isHidden = false
                    self.btnLocation.setTitleColor(.white, for: .selected)
                    self.btnLocation.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                    btnBoat.isSelected = false
                    self.btnBoat.setTitleColor(.black, for: .normal)
                    self.btnBoat.backgroundColor = .white
                    self.view.bringSubview(toFront: self.viewOtherLoc)
                    self.viewCurrLoc.isHidden = true
                    viewLocHgtConstraint.constant = 65
                    self.view.bringSubview(toFront: self.viewOtherLoc)
                    self.viewOtherLoc.isHidden = false
                    
                }
                else{
                    selectedIndex = 0
                   // tblViewHgtConstraint.constant = view.frame.size.height - viewLoc.frame.maxY
                    viewLoc.isHidden = true
                    self.btnLocation.setTitleColor(.black, for: .normal)
                    self.btnLocation.backgroundColor = .white
                    self.btnBoat.setTitleColor(.black, for: .normal)
                    self.btnBoat.backgroundColor = .white
                    btnBoat.isSelected = false
                    btnLocation.isSelected = false
                    self.viewOtherLoc.isHidden = true
                    self.viewCurrLoc.isHidden = true
                    viewLocHgtConstraint.constant = 0
                    if navigateFrom == 1
                    {
                        self.callProductListService()
                    }
                    else
                    {
                        self.callCharterListVC(strLat: "", strLong: "", strRadius: "", valueFrom: 1)
                    }
            }

        default:
           // tblViewHgtConstraint.constant = view.frame.size.height - viewLoc.frame.maxY
            viewLoc.isHidden = true
            self.btnBoat.setTitleColor(.black, for: .normal)
            self.btnBoat.backgroundColor = .white
            self.btnLocation.setTitleColor(.black, for: .normal)
            self.btnLocation.backgroundColor = .white
            self.viewOtherLoc.isHidden = true
            self.viewCurrLoc.isHidden = true
            btnBoat.isSelected = false
            btnLocation.isSelected = false
            viewLocHgtConstraint.constant = 0
            if navigateFrom == 1
            {
                self.callProductListService()
            }
            else
            {
                self.callCharterListVC(strLat: "", strLong: "", strRadius: "", valueFrom: 1)
            }
            break
        }
    }
    
    @IBAction func onChangeSliderValue(_ sender: UISlider) {
        let value = Int (sender.value)
        lblSlider.text = String (value) + " km"
        selectedIndex = 1
        if navigateFrom == 1
        {
            NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(self.callProductListService), object: nil)
            if (timer.isValid) {
                timer.invalidate()
            }
            timer = Timer(timeInterval: 0.9, target: self, selector: #selector(self.callProductListService), userInfo: nil, repeats: false)
            RunLoop.main.add(timer, forMode: RunLoopMode.defaultRunLoopMode)
        }
        else
        {
            NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(self.callCharterListService), object: nil)
            if (timer.isValid) {
                timer.invalidate()
            }
            timer = Timer(timeInterval: 0.9, target: self, selector: #selector(self.callCharterListService), userInfo: nil, repeats: false)
            RunLoop.main.add(timer, forMode: RunLoopMode.defaultRunLoopMode)
        }
    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
        
    }

    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        if navigateFrom == 1
        {
            let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MultipleUploadViewController") as! MultipleUploadViewController
            uploadVC.indexVal = 1
            self.navigationController?.pushViewController(uploadVC, animated: true)
        }
        else{
            DispatchQueue.main.async {
                let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                uploadVC.index = 1
                uploadVC.value = 1
                self.navigationController?.pushViewController(uploadVC, animated: true)
            }
        }
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
        return arrBoatList.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var iseditable = Bool()
        var isflagged = Bool()
        var spefic_id = String()
        var firebase_id = String()
        let cell = tableView.dequeueReusableCell(withIdentifier: "boatRental_Cell", for: indexPath) as! BoatRentalTableViewCell
        if navigateFrom == 1
        {
            let objProduct: ProductDetailsBO = arrBoatList[indexPath.row] as! ProductDetailsBO
            cell.lblName.text = objProduct.name
            cell.lblPrice.text = "$" + objProduct.price
            cell.lblAddress.text = objProduct.user_loc
            cell.lblPostedBy.text = "Posted by: " + objProduct.user_name
            cell.imgFeatured.isHidden = objProduct.is_featured ? false : true
            if objProduct.arrImages.count != 0
            {
                let imgData: ImageBO = objProduct.arrImages[0] as! ImageBO
                cell.imgProduct.kf.setImage(with: URL(string:imgData.image_name), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
            }
            iseditable = objProduct.is_editable
            isflagged = objProduct.is_flagged
            spefic_id = objProduct.product_id
            firebase_id = objProduct.firebase_id
            
            cell.onClickReport = {() -> Void in
                
                let alert = UIAlertController(title: "BLUE BUDDY", message: "Are you sure you want to report?", preferredStyle: UIAlertControllerStyle.alert)
                let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                    let dictVal = NSMutableDictionary()
                    dictVal["type"] = "product"
                    dictVal["type_id"] = objProduct.product_id
                    TripsParser.callReportService(dictVal, complete: { (status, strMsg) in
                        if status
                        {
                            cell.imgFlag.image = #imageLiteral(resourceName: "report_flag.png")
                            objProduct.is_flagged = true
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
            
        }
        else{
            let objCharter: CharterDetailsBO = arrBoatList[indexPath.row] as! CharterDetailsBO
            cell.lblName.text = objCharter.name
            cell.lblPrice.text = "$" + objCharter.price
            cell.lblAddress.text = objCharter.user_loc
            cell.lblPostedBy.text = "Posted by: " + objCharter.user_name
            cell.imgFeatured.isHidden = objCharter.is_featured ? false : true
            cell.imgProduct.kf.setImage(with: URL(string:objCharter.charter_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
            iseditable = objCharter.is_editable
            isflagged = objCharter.is_flagged
            spefic_id = objCharter.charter_id
            firebase_id = objCharter.firebase_id
            
            cell.onClickReport = {() -> Void in
                
                let alert = UIAlertController(title: "BLUE BUDDY", message: "Are you sure you want to report?", preferredStyle: UIAlertControllerStyle.alert)
                let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                    let dictVal = NSMutableDictionary()
                    dictVal["type"] = "charter"
                    dictVal["type_id"] = objCharter.charter_id
                    TripsParser.callReportService(dictVal, complete: { (status, strMsg) in
                        if status
                        {
                            cell.imgFlag.image = #imageLiteral(resourceName: "report_flag.png")
                            objCharter.is_flagged = true
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
        }
        
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
        cell.lblPrice.layer.cornerRadius = cell.lblPrice.frame.size.height/2
        cell.lblPrice.clipsToBounds = true
        cell.lblName.layer.cornerRadius = cell.lblName.frame.size.width/2
        cell.imgProduct.layer.cornerRadius = 5.0
        cell.imgProduct.clipsToBounds = true

        if iseditable
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
            cell.imgFlag.image = isflagged ? #imageLiteral(resourceName: "report_flag.png") : #imageLiteral(resourceName: "flag.png")
        }

        
        cell.onClickDetails = {() -> Void in
            if self.navigateFrom == 1
            {
                self.callProdctDetailsService(strId: spefic_id as NSString, false)
            }
            else
            {
                self.callCharterDetailsService(strId: spefic_id as NSString, isFromDetails: true)
            }
        }
        cell.onClickOtherBtn = {() -> Void in
            if self.navigateFrom == 1
            {
                if iseditable
                {
                    let objProduct: ProductDetailsBO = self.arrBoatList[indexPath.row] as! ProductDetailsBO
                    self.callProdctDetailsService(strId: objProduct.product_id as NSString, true)

                }
                else{
                    ProfileViewController.callChatDetails(firebase_id, self)
                }
            }
            else{
                
                if iseditable
                {
                    self.callCharterDetailsService(strId: spefic_id as NSString, isFromDetails: false)
                }
                else{
                    ProfileViewController.callChatDetails(firebase_id, self)
                }
            }
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 237
    }
    
    
    @objc private func callCharterListVC(strLat: NSString, strLong: NSString,  strRadius: NSString, valueFrom: Int)
    {
        let dictVal = NSMutableDictionary()
        dictVal["user_lat"]     = valueFrom == 0 ? String(APP_DELEGATE.currLoc.coordinate.latitude) as NSString : strLat
        dictVal["user_long"]    = valueFrom == 0 ? String(APP_DELEGATE.currLoc.coordinate.longitude) as NSString : strLong
        dictVal["radius"]       = valueFrom == 0 ? sliderView.value : strRadius

        MarketParser.callChaterListService(dictVal, complete: { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    if status
                    {
                        self.arrBoatList.removeAllObjects()
                        self.arrBoatList = arrList as! NSMutableArray
                        self.tblViewList.isHidden = false
                        self.lblNoData.isHidden = true
                        self.tblViewList.reloadData()
                    }
                    else
                    {
                        self.tblViewList.isHidden = true
                        self.lblNoData.isHidden = false
                        self.tblViewList.reloadData()
                    }
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.arrBoatList.removeAllObjects()
                    self.tblViewList.isHidden = true
                    self.lblNoData.isHidden = false
                    self.tblViewList.reloadData()
                }
            }
        })
    }
    func callProductListService()
    {
        let dictVal = NSMutableDictionary()
        switch selectedIndex {
        case 0:
            dictVal["category"] = arrCategory.componentsJoined(by: ",")
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

        MarketParser.callProductListService(dictVal) { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    if arrList.count != 0
                    {
                        self.arrBoatList.removeAllObjects()
                        self.arrBoatList = arrList as! NSMutableArray
                        self.tblViewList.isHidden = false
                        self.tblViewList.reloadData()
                        self.lblNoData.isHidden = true
                    }
                    else
                    {
                        self.arrBoatList.removeAllObjects()
                        self.tblViewList.isHidden = true
                        self.tblViewList.reloadData()
                        self.lblNoData.isHidden = false
                    }
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.arrBoatList.removeAllObjects()
                    self.tblViewList.isHidden = false
                    self.lblNoData.isHidden = true
                    self.tblViewList.reloadData()
                }
                
            }

        }
    }
    func callCharterListService()
    {
        let dictVal = NSMutableDictionary()
        dictVal["user_lat"]     =  String(APP_DELEGATE.currLoc.coordinate.latitude) as NSString
        dictVal["user_long"]    =  String(APP_DELEGATE.currLoc.coordinate.longitude) as NSString
        dictVal["radius"]       =  String(sliderView.value)
        
        MarketParser.callChaterListService(dictVal, complete: { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    if status
                    {
                        self.arrBoatList.removeAllObjects()
                        self.arrBoatList = arrList as! NSMutableArray
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
                self.arrBoatList.removeAllObjects()
                self.tblViewList.reloadData()
                self.tblViewList.isHidden = false
                self.lblNoData.isHidden = true
                
            }
        })
    }

    func callCharterDetailsService(strId: NSString, isFromDetails: Bool )
    {
        MarketParser.callChaterDetailsService(["id" : strId], complete: { (status, strMsg, objDetails) in
            if status
            {
                
                DispatchQueue.main.async {
                    if isFromDetails
                    {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "RentalDetailsViewController") as! RentalDetailsViewController
                        detailsVC.objCharterDetails = objDetails
                        detailsVC.indexVal = 0
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                    else{
                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                        uploadVC.index = 1
                        uploadVC.value = 1
                        uploadVC.charterDetails = objDetails
                        self.navigationController?.pushViewController(uploadVC, animated: true)
                    }
                }
            }
        })
    }
    
    func callProdctDetailsService(strId: NSString, _ isEditable: Bool)
    {
        MarketParser.callProductDetailsWebService(["id" : strId], complete: { (status, strMsg, objDetails) in
            if status
            {
                DispatchQueue.main.async {
                    if isEditable
                    {
                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MultipleUploadViewController") as! MultipleUploadViewController
                        let objValue = objDetails
                        objValue.product_id = strId as String
                        uploadVC.productData = objValue
                        uploadVC.indexVal = 1
                        self.navigationController?.pushViewController(uploadVC, animated: true)
                    }
                    else{
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ProductDetailsViewController") as! ProductDetailsViewController
                        let objValue = objDetails
                        objValue.product_id = strId as String
                        detailsVC.objProductDetails = objValue
                        self.navigationController?.pushViewController(detailsVC, animated: true)

                    }

                }
            }
        })
    }

    
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        self.view.endEditing(true)
        print(APP_DELEGATE.currLoc)
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
        let latitude = Double((placeCoord?.latitude)!)
        let longitude = Double((placeCoord?.longitude)!)
        if navigateFrom == 1
        {
            self.callProductListService()
        }
        else
        {
            self.callCharterListVC(strLat: String(latitude) as NSString, strLong: String(longitude) as NSString, strRadius: "50", valueFrom: 1)
        }
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
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
