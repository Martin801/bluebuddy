//
//  EditProfileViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/20/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel
import Firebase
import MobileCoreServices

class EditProfileViewController: UIViewController, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate,UIImagePickerControllerDelegate,
UINavigationControllerDelegate , UIGestureRecognizerDelegate{

    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var txtId: UITextField!
    @IBOutlet weak var btnUpdate: UIButton!
    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var contentVhgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var lblUrl: UILabel!
    @IBOutlet weak var lblPopUpTitle: UILabel!
    @IBOutlet weak var viewPopUp: UIView!
    @IBOutlet weak var btnYouTube: UIButton!
    @IBOutlet weak var btnTwitter: UIButton!
    @IBOutlet weak var btnInsta: UIButton!
    @IBOutlet weak var btnFb: UIButton!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var txtWebsite: UITextField!
    @IBOutlet weak var txtViewDes: UITextView!
    @IBOutlet weak var txtLastName: UITextField!
    @IBOutlet weak var txtFirstName: UITextField!
    @IBOutlet weak var imgPic: UIImageView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    
    var lengthyLabel: MarqueeLabel?
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var pic: UIImage?
    var selectedTag: Int?
    var strFbURL: String? = ""
    var strTwitterURL: String? = ""
    var strYouTubeURL: String? = ""
    var strInstagramURL: String? = ""
    @IBOutlet weak var viewTop: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous

        viewPopUp.isHidden              = true
        let myColor                     = UIColor.lightGray
        btnFb.layer.borderColor         = myColor.cgColor
        btnFb.layer.borderWidth         = 1.0
        btnFb.layer.cornerRadius        = 5.0
        btnInsta.layer.borderColor      = myColor.cgColor
        btnInsta.layer.borderWidth      = 1.0
        btnInsta.layer.cornerRadius     = 5.0
        btnYouTube.layer.borderColor    = myColor.cgColor
        btnYouTube.layer.borderWidth    = 1.0
        btnYouTube.layer.cornerRadius   = 5.0
        btnTwitter.layer.borderColor    = myColor.cgColor
        btnTwitter.layer.borderWidth    = 1.0
        btnTwitter.layer.cornerRadius   = 5.0

        txtViewDes.layer.borderColor    = myColor.cgColor
        txtViewDes.layer.borderWidth    = 1.0
        txtViewDes.layer.cornerRadius   = 3.0
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true
        self.viewPopUp.frame = CGRect(x: 0, y: 0, width: (APP_DELEGATE.window?.frame.size.width)!, height: (APP_DELEGATE.window?.frame.size.height)!)
        UIApplication.shared.keyWindow!.addSubview(self.viewPopUp)
        viewPopUp.translatesAutoresizingMaskIntoConstraints = true
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        txtFirstName.text = LOGIN_CONSTANT.objProfile.first_name
        txtLastName.text = LOGIN_CONSTANT.objProfile.last_name
        lengthyLabel?.text = LOGIN_CONSTANT.objProfile.location
        txtLocation.leftView = lengthyLabel
        txtLocation.leftViewMode = .always
        txtLocation.text = nil
        txtViewDes.text = LOGIN_CONSTANT.objProfile.about
        if !LOGIN_CONSTANT.objProfile.website_link.isEmpty || LOGIN_CONSTANT.objProfile.website_link != " "
        {
            txtWebsite.text = LOGIN_CONSTANT.objProfile.website_link
        }
        imgPic.kf.setImage(with: URL(string:LOGIN_CONSTANT.objProfile.profile_pic))
        strFbURL = LOGIN_CONSTANT.objProfile.fb_url
        strTwitterURL = LOGIN_CONSTANT.objProfile.twtr_url
        strInstagramURL = LOGIN_CONSTANT.objProfile.ingm_url
        strYouTubeURL = LOGIN_CONSTANT.objProfile.ggle_url
        strAddress = LOGIN_CONSTANT.objProfile.location
        placeCoord = CLLocationCoordinate2DMake(Double(LOGIN_CONSTANT.objProfile.lat)!, Double(LOGIN_CONSTANT.objProfile.long)!);
        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtViewDes.frame.width, font: txtViewDes.font!, string: LOGIN_CONSTANT.objProfile.about) + 25
        txtDescHgtConstraint.constant = valHgt
        contentVhgtConstraint.constant = btnUpdate.frame.maxY + valHgt

        imgPic.layer.cornerRadius = imgPic.frame.size.height/2
        imgPic.clipsToBounds = true
        
        btnUpdate.layer.cornerRadius = 5.0
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                      //  self.lblNotifications.text = strCounter
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
    @IBAction func onClickBtnAddImage(_ sender: UIButton) {
        
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
//            self.dismiss(animated: true, completion: nil)
            }
        })
        
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        DispatchQueue.main.async {
            self.present(alertController, animated: true, completion:{})
        }
    }

    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnUpdate(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtFirstName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your first name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtLastName.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your last name", title: "BLUE BUDDY", viewController: self)
        }
        else if strAddress.isEmpty {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else if(txtViewDes.text?.isEmpty)! || (txtViewDes.text == "Write about yourself") {
            Common.showAlert(message: "Please write about yourself", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let latitude: Double = Double((placeCoord?.latitude)!)
            let longitude: Double = Double((placeCoord?.longitude)!)

            let dictVal = NSMutableDictionary()
            dictVal["first_name"]   = txtFirstName.text
            dictVal["last_name"]    = txtLastName.text
            dictVal["about"]        = txtViewDes.text
            dictVal["website_link"] = txtWebsite.text
            dictVal["fb_url"]       = (strFbURL?.isEmpty)! ? "" : strFbURL
            dictVal["twtr_url"]     = (strTwitterURL?.isEmpty)! ? "" : strTwitterURL
            dictVal["ingm_url"]     = (strTwitterURL?.isEmpty)! ? "" : strTwitterURL
            dictVal["ggle_url"]     = (strYouTubeURL?.isEmpty)! ? "" : strYouTubeURL
            dictVal["user_location"]    = strAddress
            dictVal["user_lat"]         = String(latitude)
            dictVal["user_long"]        = String(longitude)
            if pic == nil{
                
                UserParser.callUpdateProfileService(dictVal, complete: { (status, strMsg) in
                    if status
                    {
                        DispatchQueue.main.async
                        {
                            let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                            let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                                DispatchQueue.main.async {
                                    self.callProfilePage()
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
            else{
                dictVal["profile_img"]    = pic
                UserParser.callUploadProfilePictureService(dictVal, complete: { (status, strMsg, dictResult) in
                    if status
                    {
                        DispatchQueue.main.async
                            {
                                let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                                let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                                    DispatchQueue.main.async {
                                        self.callProfilePage()
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
    }
    
    @IBAction func onTapPopUp(_ sender: UITapGestureRecognizer) {
        viewPopUp.isHidden = true
    }
    @IBAction func onClickBtnSocial(_ sender: UIButton) {
        viewPopUp.isHidden = false
       // UIApplication.shared.keyWindow!.addSubview(viewPopUp)
        //self.view.bringSubview(toFront: viewPopUp)
        selectedTag = sender.tag
        var urlString = "https://www.facebook.com/"
        switch sender.tag {
        case 1:
            urlString = "https://www.facebook.com/"
            lblPopUpTitle.text = "Enter your Facebook url"
            if LOGIN_CONSTANT.objProfile.fb_url.isEmpty || LOGIN_CONSTANT.objProfile.fb_url == " "
            {
                txtId.text = ""
            }
            else{
                txtId.text = LOGIN_CONSTANT.objProfile.fb_url
            }
        case 3:
            urlString = "https://www.twitter.com/"
            lblPopUpTitle.text = "Enter your Twitter url"
            if LOGIN_CONSTANT.objProfile.twtr_url.isEmpty || LOGIN_CONSTANT.objProfile.twtr_url == " "
            {
                txtId.text = ""
            }
            else{
                txtId.text = LOGIN_CONSTANT.objProfile.twtr_url
            }
        case 2:
            urlString = "https://www.instagram.com/"
            lblPopUpTitle.text = "Enter your Instagram url"
            if LOGIN_CONSTANT.objProfile.ingm_url.isEmpty || LOGIN_CONSTANT.objProfile.ingm_url == " "
            {
                txtId.text = ""
            }
            else{
                txtId.text = LOGIN_CONSTANT.objProfile.ingm_url
            }
        case 4:
            urlString = "https://www.youtube.com/"
            lblPopUpTitle.text = "Enter your YouTube url"

            if LOGIN_CONSTANT.objProfile.ggle_url.isEmpty || LOGIN_CONSTANT.objProfile.ggle_url == " "
            {
                txtId.text = ""
            }
            else{
                txtId.text = LOGIN_CONSTANT.objProfile.ggle_url
            }
        default:
            break
        }
        let attributedString = NSMutableAttributedString(string: urlString)
        
        attributedString.addAttribute(NSUnderlineStyleAttributeName, value: NSUnderlineStyle.styleSingle.rawValue, range: NSMakeRange(0, attributedString.length))
        lblUrl.attributedText = attributedString
    }
    
    @IBAction func onClickBtnPopUp(_ sender: UIButton) {
        self.view.endEditing(true)
        viewPopUp.isHidden = true
        if sender.tag == 1
        {
            
        }
        else
        {
            if selectedTag == 1
            {
                if !(txtId.text?.isEmpty)! {
                    if (txtId.text?.hasPrefix("https://www.facebook.com/"))! {
                        strFbURL = txtId.text
                    }
                    else {
                        Common.showAlert(message: "Please enter proper Facebook URL", title: "BLUE BUDDY", viewController: self)
                        self.view.endEditing(true)
                        viewPopUp.isHidden = false
                    }
                }
                

            }
            else if (selectedTag == 3)
            {
                if !(txtId.text?.isEmpty)! {
                        if (txtId.text?.hasPrefix("https://www.twitter.com/"))! {
                            strTwitterURL = txtId.text
                        }
                        else {
                            Common.showAlert(message: "Please enter proper Twitter URL", title: "BLUE BUDDY", viewController: self)
                            self.view.endEditing(true)
                            viewPopUp.isHidden = false
                        }
                    }
                
            }
            else if (selectedTag == 2)
            {
                if !(txtId.text?.isEmpty)! {
                    if (txtId.text?.hasPrefix("https://www.instagram.com/"))! {
                        strInstagramURL = txtId.text
                    }
                    else {
                        Common.showAlert(message: "Please enter proper Instagram URL", title: "BLUE BUDDY", viewController: self)
                        self.view.endEditing(true)
                        viewPopUp.isHidden = false
                    }
                }
                
                
            }
            else
            {
                if !(txtId.text?.isEmpty)! {
                    if (txtId.text?.hasPrefix("https://www.youtube.com/"))! {
                        strYouTubeURL = txtId.text
                    }
                    else {
                        Common.showAlert(message: "Please enter proper YouTube URL", title: "BLUE BUDDY", viewController: self)
                        self.view.endEditing(true)
                        viewPopUp.isHidden = false
                    }
                }
                
            }
        }
    }
    
    
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        if textField == txtLocation
        {
            self.view.endEditing(true)
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
    // MARK: - UIImagePickerControllerDelegate Methods
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]){
        let chosenImage = info[UIImagePickerControllerOriginalImage] as! UIImage //2
        imgPic.contentMode = .scaleAspectFill //3
        imgPic.image = chosenImage //4
        pic = chosenImage
        self.dismiss(animated:true, completion: nil)
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
//        self.dismiss(animated:true, completion: nil)
    }
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtViewDes.text == "Write about yourself"
        {
            txtViewDes.text = ""
            txtViewDes.textColor = .black
        }
    }
    private func textViewDidEndEditing(_ textView: UITextView) {
        if txtViewDes.text.isEmpty
        {
            txtViewDes.text = "Write about yourself"
            txtViewDes.textColor = .lightGray
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
        if txtViewDes.text.isEmpty
        {
            txtViewDes.text = "Enter charter description"
            txtViewDes.textColor = .lightGray
        }
        self.view.endEditing(true)
    }
    
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtViewDes.sizeThatFits(self.txtViewDes.bounds.size)
        let higntcons = contentSize.height
        txtDescHgtConstraint.constant = higntcons
        contentVhgtConstraint.constant = btnUpdate.frame.maxY + higntcons
    }

    func callProfilePage()
    {
        UserParser.callProfileDetailsService("", complete: { (status, strmsg, objProfile) in
            DispatchQueue.main.async {
                let uid: String = (Auth.auth().currentUser?.uid)!
                let userData: [String : Any] = ["email" : UserDefaults.standard.value(forKey: kUserEmail)!,
                                                "name"  : objProfile.first_name.capitalized + " " + objProfile.last_name.capitalized,
                                                "image" : objProfile.profile_pic]
                
                Database.database().reference().child("users").child(uid).child("credentials").updateChildValues(userData, withCompletionBlock: { (errr, _) in
                    if errr == nil {
                        DispatchQueue.main.async {
                            self.navigationController?.popViewController(animated: true)

                        }
                    }
                })
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
