//
//  AddCharterViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/29/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel

class AddCharterViewController: UIViewController, UITextViewDelegate, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate{

    @IBOutlet weak var btnHideDetails: UIButton!
    @IBOutlet weak var viewUpdate: UIView!
    @IBOutlet weak var contentVHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var txtCapacity: UITextField!
    @IBOutlet weak var txtBoatType: UITextField!
    @IBOutlet weak var txtHalfPrice: UITextField!
    @IBOutlet weak var txtFullprice: UITextField!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var txtDescConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtDesc: UITextView!
    @IBOutlet weak var txtCharterName: UITextField!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    var boatPic = UIImage()
    var lengthyLabel: MarqueeLabel?
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var indexval = Int()
    var hideDetails = Bool()
    var objCharterDetails = CharterDetailsBO()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if objCharterDetails.is_hidden {
            hideDetails = true
            btnHideDetails.setImage(UIImage(named: "tick1.png"), for: .normal)
        }
        else {
            hideDetails = false
            btnHideDetails.setImage(UIImage(named: "tick2.png"), for: .normal)
        }
        
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        
        txtFullprice.leftViewMode = UITextFieldViewMode.always
        let lblFullPrice = UILabel(frame: CGRect(x: 0, y: 0, width: txtFullprice.frame.size.width/2, height: txtFullprice.frame.size.height))
        lblFullPrice.text = "Full Price     $"
        lblFullPrice.textColor = .darkGray
        lblFullPrice.font = txtFullprice.font
        txtFullprice.leftView = lblFullPrice
        
        txtHalfPrice.leftViewMode = UITextFieldViewMode.always
        let lblHalfPrice = UILabel(frame: CGRect(x: 0, y: 0, width: txtHalfPrice.frame.size.width/2, height: txtFullprice.frame.size.height))
        lblHalfPrice.text = "Half Price     $"
        lblHalfPrice.textColor = .darkGray
        lblHalfPrice.font = txtHalfPrice.font
        txtHalfPrice.leftView = lblHalfPrice
        
        btnNext.layer.cornerRadius = 5.0
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        
        let myColor = UIColor.lightGray
        txtDesc.layer.cornerRadius = 5.0
        txtDesc.layer.borderWidth = 1.0
        txtDesc.layer.borderColor = myColor.cgColor
        if !objCharterDetails.user_id.isEmpty {
            txtFullprice.text           = objCharterDetails.price
            txtHalfPrice.text           = objCharterDetails.half_price
            txtBoatType.text            = objCharterDetails.boat_type
            txtDesc.textColor           = .black
            txtDesc.text                = objCharterDetails.charter_desc
            txtCharterName.text         = objCharterDetails.name
            txtLocation.leftView        = lengthyLabel
            txtLocation.leftViewMode    = .always
            lengthyLabel?.text          = objCharterDetails.user_loc
            strAddress                  = objCharterDetails.user_loc
            txtCapacity.text            = objCharterDetails.capacity
            let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtDesc.frame.width, font: txtDesc.font!, string: objCharterDetails.charter_desc) + 25
            txtDescConstraint.constant  = valHgt
            contentVHgtConstraint.constant = btnNext.frame.maxY + 60 + valHgt
            viewUpdate.isHidden = false
            btnNext.isHidden = true

        }
        else
        {
            viewUpdate.isHidden = true
            btnNext.isHidden = false
            contentVHgtConstraint.constant = btnNext.frame.maxY + 60
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
    
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtCharterName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your charter name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDesc.text == "Enter charter description")
        {
            Common.showAlert(message: "Please enter your charter desc", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtFullprice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter price for full day", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtHalfPrice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter price for half day", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtBoatType.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter type of boat", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtCapacity.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter boat capacity", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let latitude: Double = Double((placeCoord?.latitude)!)
            let longitude: Double = Double((placeCoord?.longitude)!)
            let dictVal = NSMutableDictionary()
            dictVal["ch_name"]          = txtCharterName.text
            dictVal["ch_description"]   = txtDesc.text
            dictVal["ch_location"]      = strAddress
            dictVal["ch_fullprice"]     = txtFullprice.text
            dictVal["ch_halfprice"]     = txtHalfPrice.text
            dictVal["ch_boattype"]      = txtBoatType.text
            dictVal["ch_capacity"]      = txtCapacity.text
            dictVal["ch_lat"]           = String(latitude)
            dictVal["ch_long"]          = String(longitude)
            dictVal["ch_img"]           = boatPic
            dictVal["ch_hide_details"]  = hideDetails == true ? "1":"0"
            MarketParser.callCreateCharterService(dictVal, complete: { (status, strMsg, objId) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                self.callCharterDetailsService(objId as NSString)
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
            })

        }
    }

    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this charter?", preferredStyle: UIAlertControllerStyle.alert)
        let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            let dictVal = NSMutableDictionary()
            dictVal["type"] = "charter"
            dictVal["data_id"] = self.objCharterDetails.charter_id
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
                                        if viewController.isKind(of: self.indexval == 1 ? BoatRentalViewController.self : MyListingViewController.self) {
                                            isFound = true
                                            if self.indexval == 1 {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateCharterList"), object: nil)
                                            }
                                            else if self.indexval == 2
                                            {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateMyList"), object: nil)
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
        if (txtCharterName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your charter name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDesc.text == "Enter charter description")
        {
            Common.showAlert(message: "Please enter your charter desc", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtFullprice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter price for full day", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtHalfPrice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter price for half day", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtBoatType.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter type of boat", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtCapacity.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter boat capacity", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let dictVal = NSMutableDictionary()
            dictVal["ch_name"]          = txtCharterName.text
            dictVal["ch_description"]   = txtDesc.text
            dictVal["ch_location"]      = strAddress
            dictVal["ch_fullprice"]     = txtFullprice.text
            dictVal["ch_halfprice"]     = txtHalfPrice.text
            dictVal["ch_boattype"]      = txtBoatType.text
            dictVal["ch_capacity"]      = txtCapacity.text
            dictVal["ch_lat"]           = ""
            dictVal["ch_long"]          = ""
            dictVal["ch_id"]             = objCharterDetails.charter_id
            dictVal["ch_hide_details"]  = hideDetails == true ? "1":"0"
            MarketParser.callUpdateCharterService(dictVal, _isFromPic: false, complete: { (status, strMsg) in
                if status
                {
                    let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                    let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                        DispatchQueue.main.async {
                            self.callCharterDetailsService(self.objCharterDetails.charter_id as NSString)
                        }
                    })
                    alert.addAction(okAction)
                    self.present(alert, animated: true, completion: nil)
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
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
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
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtDesc.text == "Enter charter description"
        {
            txtDesc.text = ""
            txtDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Enter charter description"
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
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Enter charter description"
            txtDesc.textColor = .lightGray
        }
        self.view.endEditing(true)
    }

    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtDesc.sizeThatFits(self.txtDesc.bounds.size)
        let higntcons = contentSize.height
        txtDescConstraint.constant = higntcons
        contentVHgtConstraint.constant = btnNext.frame.maxY + 60
    }
    
    func callCharterDetailsService( _ strId: NSString)
    {
        MarketParser.callChaterDetailsService(["id" : strId], complete: { (status, strMsg, objDetails) in
            if status
            {
                DispatchQueue.main.async {
                    let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "RentalDetailsViewController") as! RentalDetailsViewController
                    detailsVC.objCharterDetails = objDetails
                    detailsVC.indexVal = self.indexval
                    self.navigationController?.pushViewController(detailsVC, animated: true)
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

    @IBAction func btnHideDetailsAction(_ sender: UIButton) {
        hideDetails = !hideDetails
        if hideDetails {
        sender.setImage(UIImage(named: "tick1.png"), for: .normal)
        }
        else {
        sender.setImage(UIImage(named: "tick2.png"), for: .normal)
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
