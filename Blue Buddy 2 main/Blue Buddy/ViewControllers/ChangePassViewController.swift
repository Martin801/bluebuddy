//
//  ChangePassViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/21/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import Firebase
import NVActivityIndicatorView

class ChangePassViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var lblNotiBadge: UILabel!
    @IBOutlet weak var txtConfirmPass: UITextField!
    @IBOutlet weak var txtNewPass: UITextField!
    @IBOutlet weak var txtOldPass: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        lblNotiBadge.layer.cornerRadius = lblNotiBadge.frame.size.height/2
        lblNotiBadge.clipsToBounds = true
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblNotiBadge.text = strCounter
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
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtOldPass.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your old password", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtNewPass.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your new password", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtConfirmPass.text?.isEmpty)! {
            Common.showAlert(message: "Please re-enter your password", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtConfirmPass.text != txtNewPass.text) {
            Common.showAlert(message: "Please enter the same password", title: "BLUE BUDDY", viewController: self)
        }

        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["old_password"] = txtOldPass.text
            dictVal["new_password"] = txtNewPass.text
            
            let user = Auth.auth().currentUser
            let credential = EmailAuthProvider.credential(withEmail: UserDefaults.standard.value(forKey: kUserEmail)! as! String, password: dictVal["old_password"] as! String)
            let activityData = ActivityData()
            NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
            }
            user?.reauthenticate(with: credential, completion: { (error) in
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                    NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                }
                if error != nil{
                    Common.showAlert(message: "some error occured, please try again", title: "BLUE BUDDY", viewController: self)
                }else{
                    //change to new password
                    let activityData = ActivityData()
                    NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                        NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
                    }
                    user?.updatePassword(to: dictVal["new_password"] as! String, completion: { (error) in
                        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                            NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                        }
                        if ((error) != nil) {
                            // An error happened.
                            Common.showAlert(message: "some error occured, please try again", title: "BLUE BUDDY", viewController: self)
                        } else {
                            // Password updated.
                            UserParser.callChangePasswordService(dictVal, complete: { (status, strMsg) in
                                if status
                                {
                                    DispatchQueue.main.async {
                                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                                            DispatchQueue.main.async {
                                                UserDefaults.standard.set(self.txtNewPass.text, forKey: kUserPass)
                                                self.navigationController?.popViewController(animated: true)
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
                    })
                    
               
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
    
    //MARK: - 
    //MARK: - UITextFeild Delegate
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == txtConfirmPass
        {
            if txtConfirmPass.text != txtNewPass.text
            {
                Common.showAlert(message: "Please enter the same password", title: "BLUE BUDDY", viewController: self)
                txtConfirmPass.text = ""
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
