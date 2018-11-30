//
//  InviteBuddiesViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/7/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import ContactsUI

class InviteBuddiesViewController: UIViewController, CNContactPickerDelegate {

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var btnBuddylist: UIButton!
    @IBOutlet weak var btnContactList: UIButton!
    var isFromList = Bool()
    var strTripId = String()
    var arrNumber = NSMutableArray()
    var value = Int()
    override func viewDidLoad() {
        super.viewDidLoad()
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        btnBuddylist.layer.cornerRadius = 5.0
        btnContactList.layer.cornerRadius = 5.0
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // MARK: -
    // MARK: - UIButton Action
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    @IBAction func onClickBtnBuddyList(_ sender: UIButton) {
        let buddyVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "BuddyListViewController") as! BuddyListViewController
        buddyVC.isFromInvite = true
        buddyVC.trip_id = strTripId
        buddyVC.indexval = self.value
        self.navigationController?.pushViewController(buddyVC, animated: true)
    }

    @IBAction func onClickBtnContactList(_ sender: UIButton) {
        let cnPicker = CNContactPickerViewController()
        cnPicker.delegate = self
        self.present(cnPicker, animated: true, completion: nil)
    }
    // MARK: -
    // MARK: - CNContactPickerDelegate
    func contactPicker(_ picker: CNContactPickerViewController, didSelect contacts: [CNContact]) {
        contacts.forEach { contact in
            for number in contact.phoneNumbers {
                let phoneNumber = number.value.value(forKey: "digits") as? String
                print("number is = \(phoneNumber ?? "")")
                arrNumber.add(phoneNumber!)
            }
            let fullName = CNContactFormatter.string(from: contact, style: .fullName)
            print(fullName!)
//            arrNames.add(fullName!)
            print("\(contact.phoneNumbers)")
        }
        let dictVal = NSMutableDictionary()
        dictVal["trip_id"] = strTripId
        dictVal["contact_list"] = arrNumber.componentsJoined(by: ", ")
        TripsParser.callInviteBuddyService(dictVal, false) { (status, strMsg) in
            if status
            {
                 DispatchQueue.main.async {
                    if self.isFromList
                    {
                        if let viewControllers = self.navigationController?.viewControllers {
                            for viewController in viewControllers {
                                // some process
                                if viewController.isKind(of: self.value == 1 ? SearchTripsViewController.self : MyAccountViewController.self) {
                                    //  isFound = true
                                    if (self.value == 1)
                                    {
                                        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateTripList"), object: nil)
                                        self.navigationController?.popToViewController(viewController, animated: true)
                                    }
                                    else if (self.value == 2){
                                        let tripsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MyTripsViewController") as! MyTripsViewController
                                        self.navigationController?.pushViewController(tripsVC, animated: true)
                                    }
                                    else{
                                        
                                    }
                                }
                            }
                        }
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                    else{
                        
                        UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
                            if status
                            {
                                DispatchQueue.main.async {
                                    UserDefaults.standard.set(false, forKey: "isProfileOpen")
                                    let newViewController = self.storyboard!.instantiateViewController(withIdentifier: "TabViewController") as! TabViewController
                                    newViewController.selectedIndex = 4
                                    self.present(newViewController, animated: false, completion: nil)
                                }
                            }
                            
                        })
                    }
                }
            }
            else
            {
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        }
        
//        lblContacts.text = arrNames.componentsJoined(by: ", ")
    }
    func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        print("Cancel Contact Picker")
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
