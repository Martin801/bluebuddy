//
//  BaseViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/9/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class BaseViewController: UIViewController {

    var viewHeader: UIView?
    var imgHeader: UIImageView?
    var btnBack: UIButton?
    var btnHome: UIButton?
    var lblTitle: UILabel?
    override func viewDidLoad() {
        super.viewDidLoad()
//        viewHeader = UIView()
//        loadHeader()
//        view.addSubview(viewHeader!)
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func loadHeader() {
        viewHeader?.frame = CGRect(x: CGFloat(0), y: CGFloat(20), width: CGFloat(view.frame.size.width), height: CGFloat(50))
        viewHeader?.backgroundColor = UIColor.white
        imgHeader = UIImageView()
        imgHeader?.frame = CGRect(x: CGFloat(0), y: CGFloat(0), width: CGFloat(view.frame.size.width), height: CGFloat(50))
        imgHeader?.image = #imageLiteral(resourceName: "backGround.png")
        viewHeader?.addSubview(imgHeader!)
        
        lblTitle = UILabel(frame: CGRect(x: CGFloat((imgHeader?.frame.midX)!), y: CGFloat(0), width: CGFloat(0), height: CGFloat(50)))
        lblTitle = UILabel(frame: CGRect(x: CGFloat(50), y: CGFloat(0), width: CGFloat(view.frame.size.width - 100), height: CGFloat(50)))
        lblTitle?.textAlignment = .center
        lblTitle?.textColor = UIColor.white
//        lblTitle?.font = UIFont.init(name: "Avenir", size: 20)
        viewHeader?.addSubview(lblTitle!)
//
//        btnBack = UIButton(frame: CGRect(x: CGFloat(0), y: CGFloat(0), width: CGFloat(50), height: CGFloat(50)))
//        btnHome = UIButton(frame: CGRect(x: CGFloat(view.frame.maxX - 50), y: CGFloat(0), width: CGFloat(50), height: CGFloat(50)))
//        
//        btnBack?.setImage(UIImage(named: "top-butt-back.png"), for: .normal)
//        btnBack?.addTarget(self, action: #selector(self.onClickBack), for: .touchUpInside)
//        viewHeader?.addSubview(btnBack!)
//        
//        btnHome?.setImage(UIImage(named: "top-butt-home.png"), for: .normal)
//        btnHome?.addTarget(self, action: #selector(self.onClickbtnHome), for: .touchUpInside)
//        viewHeader?.addSubview(btnHome!)
        
        
    }

    func setLabelTitleText(_ title: String) {
        lblTitle?.text = title
        lblTitle?.textAlignment = .center
    }

    func designTabBarwithSelectedIndex(selectedIndex: Int) {
//        let viewBtm = Bundle.main.loadNibNamed("BottomView", owner: self, options: nil)?[0] as! BottomView
//        viewBtm.btn3.isSelected = true
//        viewBtm.frame = CGRect(x: 0, y: self.view.frame.size.height - 50, width: self.view.frame.size.width, height: 50)
//        self.view.addSubview(viewBtm)
//        viewBtm.translatesAutoresizingMaskIntoConstraints = true
//        let horConstraint = NSLayoutConstraint(item: viewBtm, attribute: .centerX, relatedBy: .equal,
//                                               toItem: view, attribute: .centerX,
//                                               multiplier: 1.0, constant: 0.0)
//        let verConstraint = NSLayoutConstraint(item: viewBtm, attribute: .centerY, relatedBy: .equal,
//                                               toItem: view, attribute: .centerY,
//                                               multiplier: 1.0, constant: 0.0)
//        let widConstraint = NSLayoutConstraint(item: viewBtm, attribute: .width, relatedBy: .equal,
//                                               toItem: view, attribute: .width,
//                                               multiplier: 1, constant: 0.0)
//        let heiConstraint = NSLayoutConstraint(item: viewBtm, attribute: .height, relatedBy: .equal,
//                                               toItem: view, attribute: .height,
//                                               multiplier: 1, constant: 0.0)
//        
//        view.addConstraints([horConstraint, verConstraint, widConstraint, heiConstraint])
       
        

    }
    
    class func callNotificationDetails() {        
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
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
