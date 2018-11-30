//
//  ChooseCoursesViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/30/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class ChooseCoursesViewController: UIViewController {

    @IBOutlet weak var img3: UIImageView!
    @IBOutlet weak var img2: UIImageView!
    @IBOutlet weak var img1: UIImageView!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var constraintLblChooseCrseTop: NSLayoutConstraint!
    
    @IBOutlet weak var constraintLblChooseCrseHght: NSLayoutConstraint!
    
    var arrSel = NSMutableArray()
    override func viewDidLoad() {
        super.viewDidLoad()

        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        btnNext.layer.cornerRadius = 5.0
        print("screenType:", UIDevice.current.screenType.rawValue)
        if UIDevice.current.screenType.rawValue == "iPhone 4 or iPhone 4S" {
            constraintLblChooseCrseTop.constant = 0
            constraintLblChooseCrseHght.constant = 40
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
    
    @IBAction func onClickSelectCourse(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        switch sender.tag {
        case 1:
            img1.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
            if arrSel.contains("Scuba Diving") {
                arrSel.remove("Scuba Diving")
            }
            else
            {
                arrSel.add("Scuba Diving")
            }
        case 2:
            img2.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
            if arrSel.contains("Freediving") {
                arrSel.remove("Freediving")
            }
            else
            {
                arrSel.add("Freediving")
            }
            
        case 3:
            img3.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
            if arrSel.contains("Others") {
                arrSel.remove("Others")
            }
            else
            {
                arrSel.add("Others")
            }

        default:
            break
        }
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        if arrSel.count == 0
        {
            Common.showAlert(message: "Please select a category", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let listVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CourseListViewController") as! CourseListViewController
            listVC.arrCategoryList = arrSel 
            self.navigationController?.pushViewController(listVC, animated: true)        }
        
    }
    @IBAction func onCliCkBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
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
