//
//  CertificationViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/2/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class CertificationViewController: UIViewController {

    @IBOutlet weak var btnLogOut: UIButton!
    @IBOutlet weak var imgLogout: UIImageView!
    @IBOutlet weak var btnFree: UIButton!
    @IBOutlet weak var btnSchuba: UIButton!
    @IBOutlet weak var icon2: UIImageView!
    @IBOutlet weak var icon1: UIImageView!
    var indexSelected: Int?
    var itemSelected: Int?
    var isFromProfile = Bool()
    override func viewDidLoad() {
        super.viewDidLoad()
        if isFromProfile
        {
            imgLogout.isHidden = true
            btnLogOut.isHidden = true
        }
        else
        {
            imgLogout.isHidden = false
            btnLogOut.isHidden = false
        }
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action Methods
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnCertification(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        switch sender.tag {
        case 1:
            indexSelected = 1
            itemSelected = 1
            icon1.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
        case 2:
            indexSelected = 2
            itemSelected = 1
            icon2.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
        default:
            break
        }
    }

    @IBAction func onClickBtnLogOut(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
        UserDefaults.standard.set(false, forKey: kisLogin)
        UserDefaults.standard.set(false, forKey: kisFBLogin)
        UserDefaults.standard.set("", forKey: kFBID)
        UserDefaults.standard.set("", forKey: kUserPass)
        UserDefaults.standard.set("", forKey: kUserEmail)

    }

    @IBAction func onClickBtnSkip(_ sender: UIButton) {
        if isFromProfile
        {
            self.navigationController?.popViewController(animated: true)
        }
        else
        {
            UserDefaults.standard.set("4", forKey:kPageNo)
            let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AboutYourselfViewController") as UIViewController
            self.navigationController?.pushViewController(aboutVC, animated: true)
        }
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        if btnFree.isSelected && btnSchuba.isSelected {
            itemSelected = 2
            self.callService()

        }
        else if (btnFree.isSelected || btnSchuba.isSelected)
        {
            itemSelected = 1
            self.callService()
        }
        else{
            Common.showAlert(message: "Please select an option", title: "BLUE BUDDY", viewController: self)
        }

    }
    
    // MARK: -
    // MARK: - Other Methods

    func callService()
    {
            if self.indexSelected == 1 || self.itemSelected == 2
            {
                if !isFromProfile
                {
                    UserDefaults.standard.set("11", forKey:kPageNo)
                }

                DispatchQueue.main.async {
                    let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
                    certificationDetailsVC?.strSelectedItem = self.itemSelected!
                    certificationDetailsVC?.fromVC = self.isFromProfile ? 1 : 0
                    self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
                }
                
            }
            else if (self.indexSelected == 2){
                if !isFromProfile
                {
                    UserDefaults.standard.set("12", forKey:kPageNo)
                }

                DispatchQueue.main.async {
                    let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as? FreeDiverCertificationDetailsViewController
                    certificationDetailsVC?.fromVC = self.isFromProfile ? 1 : 0
                    self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
                }
            }
            else{
                    if !isFromProfile
                    {
                        UserDefaults.standard.set("11", forKey:kPageNo)
                    }
                    DispatchQueue.main.async {
                        let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
                        certificationDetailsVC?.strSelectedItem = self.itemSelected!
                        certificationDetailsVC?.fromVC = self.isFromProfile ? 1 : 0
                        self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
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
