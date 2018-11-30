//
//  UploadPicViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/3/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import Firebase
import MobileCoreServices

class UploadPicViewController: UIViewController, UIImagePickerControllerDelegate,
UINavigationControllerDelegate {
    
    @IBOutlet weak var btnLogOut: UIButton!
    @IBOutlet weak var imgLogOut: UIImageView!
    @IBOutlet weak var lblHeader: UILabel!
    var pic: UIImage?
    var index = Int()
    var value = Int()
    var strEmail = String()
    var strPassword = String()
    var charterDetails = CharterDetailsBO()
    var courseDetails  = CoursesDetailsBO()
    @IBOutlet weak var imgProfile: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        imgProfile.layer.cornerRadius = imgProfile.frame.size.height/2
        imgProfile.clipsToBounds = true
        if !charterDetails.user_id.isEmpty{
            imgProfile.kf.setImage(with: URL(string: charterDetails.charter_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil, completionHandler: nil)
        }
        else if (!courseDetails.user_id.isEmpty)
        {
            imgProfile.kf.setImage(with: URL(string: courseDetails.course_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil, completionHandler: nil)
        }
        imgLogOut.isHidden = index == 0 ? false : true
        btnLogOut.isHidden = index == 0 ? false : true
        lblHeader.text = index == 0 ? "Upload your profile picture" : "Upload your picture"
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action Methods
    
    @IBAction func onClickBtnLogOut(_ sender: UIButton) {
        UserDefaults.standard.set(false, forKey: kisLogin)
        UserDefaults.standard.set(false, forKey: kisFBLogin)
        UserDefaults.standard.set("", forKey: kFBID)
        UserDefaults.standard.set("", forKey: kUserPass)
        UserDefaults.standard.set("", forKey: kUserEmail)

        self.navigationController?.popToRootViewController(animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickBtnSelectPic(_ sender: UIButton) {
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
//                self.dismiss(animated: true, completion: nil)
            }
        })
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        DispatchQueue.main.async {
            self.present(alertController, animated: true, completion:{})
        }
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        if !charterDetails.user_id.isEmpty
        {
            if index == 1 {
                if pic != nil
                {
                    let dictVal = NSMutableDictionary()
                    dictVal["ch_id"]    = charterDetails.charter_id
                    dictVal["ch_img"]        = pic
                    MarketParser.callUpdateCharterService(dictVal, _isFromPic: true, complete: { (status, strMsg) in
                        if status
                        {
                            DispatchQueue.main.async {
                                let addVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddCharterViewController") as! AddCharterViewController
                                addVC.objCharterDetails = self.charterDetails
                                addVC.indexval = self.value
                                self.navigationController?.pushViewController(addVC, animated: true)
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
                    let addVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddCharterViewController") as! AddCharterViewController
                    addVC.objCharterDetails = self.charterDetails
                    addVC.indexval = self.value
                    self.navigationController?.pushViewController(addVC, animated: true)
                }
            }
            else
            {
                
            }
        }
        else if (!courseDetails.user_id.isEmpty)
        {
            if (index == 2 ){
                
                if pic != nil
                {
                    let dictVal = NSMutableDictionary()
                    dictVal["co_id"]    = courseDetails.course_id
                    dictVal["co_img"]        = pic
                    MarketParser.callUpdateCourseService(dictVal, _isFromPic: true, complete: { (status, strMsg) in
                        if status
                        {
                            DispatchQueue.main.async {
                                let addVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddCoursesViewController") as! AddCoursesViewController
                                addVC.objCourseDetails = self.courseDetails
                                addVC.indexVal = self.value
                                self.navigationController?.pushViewController(addVC, animated: true)
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
                    let addVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddCoursesViewController") as! AddCoursesViewController
                    addVC.objCourseDetails = self.courseDetails
                    addVC.indexVal = self.value
                    self.navigationController?.pushViewController(addVC, animated: true)
                }
            }
            else
            {
                
            }

        }
        else{
            if pic == nil {
                Common.showAlert(message: "Please select your profile picture", title: "BLUE BUDDY", viewController: self)
            }
            else
            {
                if index == 1 {
                    let addVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddCharterViewController") as! AddCharterViewController
                    addVC.boatPic = pic!
                    addVC.indexval = value
                    self.navigationController?.pushViewController(addVC, animated: true)
                }
                else  if (index == 2 ){
                    let addVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddCoursesViewController") as! AddCoursesViewController
                    addVC.coursePic = pic!
                    addVC.indexVal = value
                    self.navigationController?.pushViewController(addVC, animated: true)
                }
                else
                {
                    let dictVal = NSMutableDictionary()
                    dictVal["profile_img"]    = pic
//                    dictVal["next_step"]        = "9"
                    
                    
                    UserParser.callUploadProfilePictureService(dictVal, complete: { (status, strMsg, dictResult) in
                        if status{
                            let dictUserData = dictResult.value(forKey: "user_details") as! NSDictionary
                            
                            self.strEmail = dictUserData["Email"] as! String
                            self.strPassword = dictUserData["Password"] as! String
                            DispatchQueue.main.async {
                                Auth.auth().signIn(withEmail: self.strEmail, password: self.strPassword, completion: { (user, error) in
                                    if let err:Error = error {
//                                        Common.showAlert(message: String(err.localizedDescription), title: "BLUE BUDDY", viewController: self)
                                        
                                        
                                        Auth.auth().createUser(withEmail: self.strEmail, password: self.strPassword, completion: { (user, error) in
                                            if let err:Error = error {
                                                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
//                                                    NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                                                }
                                                Common.showAlert(message: String(err.localizedDescription), title: "BLUE BUDDY", viewController: self)
                                                print(err.localizedDescription)
                                                //  return
                                            }
                                            else{
                                                let uid: String = (Auth.auth().currentUser?.uid)!
                                                self.updateFireBaseRegToServer()
                                            }
                                        })
                                        
                                    }
                                    else{
                                        self.updateFireBaseRegToServer()
                                        }
                                        
                                    })
                                }
                            }
                            
                           
                        })
                    }
                }
            }
        }
    
    
    func updateFireBaseRegToServer() {
        
        let dictVal = NSMutableDictionary()
        dictVal["Firebase_reg_id"]    = (Auth.auth().currentUser?.uid)!
        dictVal["next_step"]        = "9"
        
        UserParser.callUploadFirebaseIDService(dictVal, complete: { (status, strMsg) in
            if status
            {
                let stepNo = UserDefaults.standard.value(forKey: kPageNo) as? String
                
                UserDefaults.standard.set(true, forKey: kisLogin)
                DispatchQueue.main.async {
                    UserDefaults.standard.set(self.strEmail, forKey: kUserEmail)
                    UserDefaults.standard.set(self.strPassword, forKey: kUserPass)
                    self.callProfileDetails()
                }
            }
        })
        
    }
    
    // MARK: -
    // MARK: - UIImagePickerControllerDelegate Methods
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]){
        let chosenImage = info[UIImagePickerControllerOriginalImage] as! UIImage //2
        imgProfile.layer.cornerRadius = imgProfile.frame.size.height/2
        imgProfile.clipsToBounds = true
        imgProfile.contentMode = .scaleAspectFill //3
        imgProfile.image = chosenImage //4
        pic = chosenImage
        self.dismiss(animated:true, completion: nil)
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        self.dismiss(animated:true, completion: nil)
    }
    
    
    // MARK: -
    // MARK: - Others Methods
    func callProfileDetails() {
        let dictVal = NSMutableDictionary()
        print(dictVal)
        
        UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
            if status
            {
                DispatchQueue.main.async {
                    let uid: String = (Auth.auth().currentUser?.uid)!
                    let userData: [String : Any] = ["email" : UserDefaults.standard.value(forKey: kUserEmail)!,
                                                    "name"  : objProfile.first_name.capitalized + " " + objProfile.last_name.capitalized,
                                                    "image" : objProfile.profile_pic]
                    
                 Database.database().reference().child("users").child(uid).child("credentials").updateChildValues(userData, withCompletionBlock: { (errr, _) in
                        if errr == nil {
                            DispatchQueue.main.async {
                                let newViewController = self.storyboard!.instantiateViewController(withIdentifier: "TabViewController") as! TabViewController
                                newViewController.selectedIndex = 4
                                self.present(newViewController, animated: false, completion: nil)
                            }
                        }
                    })
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
