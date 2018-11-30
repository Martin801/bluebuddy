//
//  MarketViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/17/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MarketViewController: UIViewController {

    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var btnServices: UIButton!
    @IBOutlet weak var btnProduct: UIButton!
    @IBOutlet weak var btnCourses: UIButton!
    @IBOutlet weak var btnBoatRental: UIButton!
    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var btnBack: UIButton!
    
    @IBOutlet weak var topConstraintBtnBoatRental: NSLayoutConstraint!
    
    
    var indexVal = Int()
    override func viewDidLoad() {
        super.viewDidLoad()
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true
        btnServices.layer.cornerRadius = 5;
        btnServices.clipsToBounds = true
        btnProduct.layer.cornerRadius = 5;
        btnProduct.clipsToBounds = true
        btnCourses.layer.cornerRadius = 5;
        btnCourses.clipsToBounds = true
        btnBoatRental.layer.cornerRadius = 5;
        btnBoatRental.clipsToBounds = true
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblNotifications.text = strCounter
                    }
                }
            }
        }
        if indexVal == 2
        {
            btnBack.isHidden = false
            lblTitle.isHidden = true
            
        }
        else{
            btnBack.isHidden = true
            lblTitle.isHidden = false
        }
        
        
        
        print("screenType:", UIDevice.current.screenType.rawValue)
        if UIDevice.current.screenType.rawValue == "iPhone 4 or iPhone 4S" {
            topConstraintBtnBoatRental.constant = 20
            //constraintLblChooseCrseHght.constant = 40
        }

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - 
    // MARK: -  UIButton Actions
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated:true)
    }
    
    @IBAction func onClickBtnMarkets(_ sender: UIButton) {
       
        if indexVal == 2
        {
            if sender.tag == 1 || sender.tag == 2 
            {
                DispatchQueue.main.async {
                    let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                    uploadVC.index = sender.tag
                    uploadVC.value = 2
                    self.navigationController?.pushViewController(uploadVC, animated: true)
                }
            }
            else if sender.tag == 3
            {
                DispatchQueue.main.async {
                    let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MultipleUploadViewController") as! MultipleUploadViewController
                    uploadVC.indexVal = 2
                    self.navigationController?.pushViewController(uploadVC, animated: true)
                }
            }
            else if sender.tag == 4
            {
                DispatchQueue.main.async {
                    let addServiceVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddServiceViewController") as! AddServiceViewController
                    addServiceVC.indexVal = 2
                    self.navigationController?.pushViewController(addServiceVC, animated: true)
                }
            }
        }
        else{
            if sender.tag == 1
            {
                let rentalVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "BoatRentalViewController") as! BoatRentalViewController
                self.navigationController?.pushViewController(rentalVC, animated: true)
            }
            else if (sender.tag == 2)
            {
                let courseVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ChooseCoursesViewController") as! ChooseCoursesViewController
                self.navigationController?.pushViewController(courseVC, animated: true)
            }
            else if (sender.tag == 3)
            {
                let listVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "TRIPSViewController") as! TRIPSViewController
                listVC.fromVC = 1
                self.navigationController?.pushViewController(listVC, animated: true)
            }
            else if (sender.tag == 4)
            {
                let serviceVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ServicesViewController") as! ServicesViewController
                self.navigationController?.pushViewController(serviceVC, animated: true)
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
