//
//  FeatureListViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/30/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class FeatureListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource
{

    @IBOutlet weak var btnDuration: UIButton!
    @IBOutlet weak var viewHghtConstraint: NSLayoutConstraint!
    @IBOutlet weak var tblHghtConstraints: NSLayoutConstraint!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var img2: UIImageView!
    @IBOutlet weak var lblPrice: UILabel!
    @IBOutlet weak var lblDuration: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var img1: UIImageView!
    @IBOutlet weak var view2: UIView!
    @IBOutlet weak var view1: UIView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    
    let arrNames = ["1 Week", "2 Week", "3 Week", "4 Week"]
    override func viewDidLoad() {
        
        let myColor = UIColor.lightGray
        tblViewList.layer.borderColor = myColor.cgColor
        tblViewList.layer.borderWidth = 1.0
        lblPrice.layer.borderColor = myColor.cgColor
        lblPrice.layer.borderWidth = 1.0
        btnDuration.contentHorizontalAlignment = .right
        btnSubmit.layer.cornerRadius = 5.0
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        view1.layer.cornerRadius = 5.0
        view1.layer.borderColor = myColor.cgColor
        view1.layer.borderWidth = 1.0
        
        view2.layer.cornerRadius = 5.0
        view2.layer.borderColor = myColor.cgColor
        view2.layer.borderWidth = 1.0
        lblPrice.text = ""
        super.viewDidLoad()
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
    
    // MARK: -
    // MARK: - UIButton Action
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onClickBtnType(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        switch sender.tag {
        case 1:
            img1.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
        case 2:
            img2.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
        default:
            break
        }

    }
    
    @IBAction func onClickBtnList(_ sender: UIButton) {
        UIView.animate(withDuration: 0.4, animations: {
            let higntcons = 90
            self.tblHghtConstraints.constant = CGFloat(higntcons)
            
            self.viewHghtConstraint.constant = 265
        }, completion: { (completed) in
            
        })
    }
    
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        
    }
    
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrNames.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "simpleCell", for: indexPath)
        cell.textLabel?.text = arrNames[indexPath.row]
        cell.textLabel?.textColor = .darkGray
        cell.textLabel?.font = UIFont.systemFont(ofSize: 15.0)
        
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        lblDuration.text = arrNames[indexPath.row]
        lblDuration.textColor = .black
        lblPrice.text = "$ 5"
        UIView.animate(withDuration: 0.4, animations: {
            self.tblHghtConstraints.constant = 0
            self.viewHghtConstraint.constant =  175

        }, completion: { (completed) in
            
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
