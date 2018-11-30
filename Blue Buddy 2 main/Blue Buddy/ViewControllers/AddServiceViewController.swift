//
//  AddServiceViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel
import MobileCoreServices

class AddServiceViewController: UIViewController, UITextViewDelegate, UIImagePickerControllerDelegate,
UINavigationControllerDelegate, GMSPlacePickerViewControllerDelegate , UITextFieldDelegate {

    @IBOutlet weak var btnHideDetails: UIButton!
    @IBOutlet weak var viewUpdate: UIView!
    @IBOutlet weak var contentVHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var imgHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var imgViewService: UIImageView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var btnUploadImage: UIButton!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var txtDesc: UITextView!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var txtServiceName: UITextField!
    var pic: UIImage?
    var lengthyLabel: MarqueeLabel?
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var objServiceData = ServiceDetailsBO()
    var indexVal = Int()
    var hideDetails = Bool()
    var onDeletingService: (() -> Void)? = nil
    override func viewDidLoad() {
        super.viewDidLoad()

        if objServiceData.is_hidden {
            hideDetails = true
            btnHideDetails.setImage(UIImage(named: "tick1.png"), for: .normal)
        }
        else {
            hideDetails = false
            btnHideDetails.setImage(UIImage(named: "tick2.png"), for: .normal)
        }
        
        let myColor = UIColor.lightGray
        txtDesc.layer.borderColor = myColor.cgColor
        txtDesc.layer.borderWidth = 1.0
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        btnSubmit.layer.cornerRadius = 5.0
        btnUploadImage.layer.cornerRadius = btnUploadImage.frame.size.height/2
        btnUploadImage.clipsToBounds = true
        imgHgtConstraint.constant = 0
        imgViewService.isHidden = true
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        
        if !objServiceData.service_id.isEmpty {
            txtServiceName.text         = objServiceData.service_type
            txtDesc.textColor           = .black
            txtDesc.text                = objServiceData.service_desc
            txtLocation.leftView        = lengthyLabel
            txtLocation.leftViewMode    = .always
            lengthyLabel?.text          = objServiceData.user_loc
            strAddress                  = objServiceData.user_loc
            imgViewService.kf.setImage(with: URL(string: objServiceData.service_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil, completionHandler: nil)
            let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtDesc.frame.width, font: txtDesc.font!, string: objServiceData.service_desc) + 25
            txtDescHgtConstraint.constant  = valHgt
            contentVHgtConstraint.constant = viewUpdate.frame.maxY + 120 + valHgt
            viewUpdate.isHidden = false
            
            imgHgtConstraint.constant = imgViewService.frame.size.width
            imgViewService.isHidden = false

            btnSubmit.isHidden = true
            imgViewService.layer.cornerRadius = imgViewService.frame.size.width/2
            imgViewService.clipsToBounds = true
        }
        else{
            viewUpdate.isHidden = true
            btnSubmit.isHidden = false
            contentVHgtConstraint.constant = btnSubmit.frame.maxY + 70
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
    @IBAction func onClickBtnUploadPic(_ sender: UIButton) {
        self.view.endEditing(true)
        let picker = UIImagePickerController()
        let alertController = UIAlertController(title: "Blue Buddy", message: "Choose Profile Picture", preferredStyle: UIAlertControllerStyle.actionSheet)
        let cameraAction = UIAlertAction(title: "Use Camera Roll", style: .default, handler: {(alert: UIAlertAction!) in
            if(UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.camera))
            {
                picker.allowsEditing = false
                picker.sourceType = UIImagePickerControllerSourceType.camera
                picker.cameraCaptureMode = .photo
                picker.delegate = self
                picker.modalPresentationStyle = .fullScreen
                self.present(picker,animated: true,completion: nil)
            }
        })
        let galleryAction = UIAlertAction(title: "Use Photo Library", style: .default, handler: {(alert: UIAlertAction!) in
            picker.allowsEditing = false
            picker.sourceType = .photoLibrary
            picker.delegate = self
//            picker.mediaTypes = UIImagePickerController.availableMediaTypes(for: .photoLibrary)!
            picker.mediaTypes = [kUTTypeImage as String]
            self.present(picker, animated: true, completion: nil)
        })
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: {(alert: UIAlertAction!) in DispatchQueue.main.async {
            self.dismiss(animated: true, completion: nil)
            }
        })
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        DispatchQueue.main.async {
            self.present(alertController, animated: true, completion:{})
        }

    }
    
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        
        let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this charter?", preferredStyle: UIAlertControllerStyle.alert)
        let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            let dictVal = NSMutableDictionary()
            dictVal["type"] = "service"
            dictVal["data_id"] = self.objServiceData.service_id
            TripsParser.callDeleteService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                if ((self.onDeletingService) != nil) {
                                    self.onDeletingService!()
                                    
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
        let cancelAction = UIAlertAction(title: NSLocalizedString("NO", comment: "No action"), style: .cancel, handler: nil)
        alert.addAction(okAction)
        alert.addAction(cancelAction)
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func onClickBtnUpdate(_ sender: UIButton) {
        if (txtServiceName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your service name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDesc.text == "Provide more details about your service")
        {
            Common.showAlert(message: "Please enter your service description", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else{
            
            let dictVal = NSMutableDictionary()
            dictVal["sv_id"]            = objServiceData.service_id
            dictVal["sv_type"]          = txtServiceName.text
            dictVal["sv_description"]   = txtDesc.text
            dictVal["sv_location"]      = strAddress
            dictVal["sv_hide_details"]  = hideDetails == true ? "1":"0"
            if (placeCoord != nil) {
                let latitude: Double = Double((placeCoord?.latitude)!)
                let longitude: Double = Double((placeCoord?.longitude)!)
                dictVal["sv_lat"]           = String(latitude)
                dictVal["sv_long"]          = String(longitude)
            }
            if pic != nil{
                dictVal["sv_img"]           = pic
            }
            MarketParser.callUpdateServiceWebService(dictVal, _isFromPic: pic == nil ? false : true, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                               self.callServiceDetails(self.objServiceData.service_id)
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
    }
    
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if pic == nil{
            Common.showAlert(message: "Please enter your service image", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtServiceName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your service name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtDesc.text == "Provide more details about your service")
        {
            Common.showAlert(message: "Please enter your service description", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else{
            
            let dictVal = NSMutableDictionary()
            dictVal["sv_type"]         = txtServiceName.text
            dictVal["sv_description"]   = txtDesc.text
            dictVal["sv_location"]      = strAddress
            dictVal["sv_hide_details"]  = hideDetails == true ? "1":"0"
            
            if (placeCoord != nil) {
                let latitude: Double = Double((placeCoord?.latitude)!)
                let longitude: Double = Double((placeCoord?.longitude)!)
                dictVal["sv_lat"]           = String(latitude)
                dictVal["sv_long"]          = String(longitude)
            }
            dictVal["sv_img"]      = pic
            MarketParser.callCreateServiceWebService(dictVal, complete: { (status, strMsg, strId) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                self.callServiceDetails(strId)
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
    }
    
    // MARK: -
    // MARK: - UITextView Delegates
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtDesc.text == "Provide more details about your service"
        {
            txtDesc.text = ""
            txtDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Provide more details about your service"
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
        let higntcons = contentSize.height < 85 ? 85 : contentSize.height
        txtDescHgtConstraint.constant = higntcons
        let hgt = imgHgtConstraint.constant == 0 ? 0 : imgViewService.frame.size.width
        contentVHgtConstraint.constant = btnSubmit.frame.maxY + 70 + hgt
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Provide more details about your service"
            txtDesc.textColor = .lightGray
        }
        self.view.endEditing(true)
    }
    
    
    func callServiceDetails( _ strId: String)
    {
        MarketParser.callServiceDetailsWebService(["id" : strId], complete: { (status, strMsg, objServiceDetails) in
            if status
            {
                DispatchQueue.main.async {
                    let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
                    detailsVC.objServiceDetails = objServiceDetails
                    detailsVC.indexVal = self.indexVal
                    self.navigationController?.pushViewController(detailsVC, animated: true)
                    
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        })
    }

    // MARK: -
    // MARK: - UIImagePickerControllerDelegate Methods
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]){
        let chosenImage = info[UIImagePickerControllerOriginalImage] as! UIImage //2
        imgHgtConstraint.constant = imgViewService.frame.size.width
        imgViewService.isHidden = false
        imgViewService.layer.cornerRadius = imgViewService.frame.size.width/2
        imgViewService.clipsToBounds = true
        imgViewService.contentMode = .scaleAspectFill //3
        imgViewService.image = chosenImage //4
        pic = chosenImage
        
        contentVHgtConstraint.constant = btnSubmit.frame.maxY + 70 + imgViewService.frame.size.width
        self.dismiss(animated:true, completion: nil)
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        self.dismiss(animated:true, completion: nil)
    }

    
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        if textField == txtLocation
        {
            DispatchQueue.main.async {
                self.view.endEditing(true)
            }
            
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
