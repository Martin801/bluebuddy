//
//  TRIPSViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/8/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class TRIPSViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var tblViewTrips: UITableView!
    var arrNames = NSArray()
    var arrSelectedVal = NSMutableArray()
    var fromVC = Int ()

    override func viewDidLoad() {
        super.viewDidLoad()
        if fromVC == 1{
            arrNames =  ["Scuba Diving", "Freediving", "Spearfishing", "Fishing", "Boats", "Others"]
            btnBack.isHidden = false
        }
        else{
            arrNames =  ["Freediving", "Scuba Diving", "Spearfishing", "Photography", "Fishing", "Snorkeling", "Kayaking", "Trolling", "Others"]
            btnBack.isHidden = true
        }
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        if arrSelectedVal.count == 0
        {
             Common.showAlert(message: "Please select a category", title: "BLUE BUDDY", viewController: self)
        }
        else{
            if fromVC == 1 {
               
                let rentalVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "BoatRentalViewController") as! BoatRentalViewController
                rentalVC.navigateFrom = 1
                rentalVC.arrCategory = arrSelectedVal
                self.navigationController?.pushViewController(rentalVC, animated: true)
            }
            else
            {
                DispatchQueue.main.async {
                    let searchTripVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "SearchTripsViewController") as! SearchTripsViewController
                    searchTripVC.strCategory = self.arrSelectedVal.componentsJoined(by: ",") as NSString
                    self.navigationController?.pushViewController(searchTripVC, animated: true)
                }
            }
        }
    }

    // MARK: -
    // MARK: - UITableView Delegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrNames.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "myAccount_Cell", for: indexPath) as! MyAccountTableViewCell
        cell.lblNames.text = arrNames[indexPath.row] as? String;
        if arrSelectedVal.contains(arrNames[indexPath.row]) {
            cell.iconAccount.image = #imageLiteral(resourceName: "check.png")
        }
        else{
            cell.iconAccount.image = #imageLiteral(resourceName: "square.png")
        }

        cell.lblLine.isHidden =  true
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath) as! MyAccountTableViewCell
        if arrSelectedVal.contains(arrNames[indexPath.row]) {
            arrSelectedVal.remove(arrNames[indexPath.row])
            cell.iconAccount.image = #imageLiteral(resourceName: "square.png")
        }
        else{
        arrSelectedVal.add(arrNames[indexPath.row])
            cell.iconAccount.image = #imageLiteral(resourceName: "check.png")
        }
    }
//    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath) {
//        let cell = tableView.cellForRow(at: indexPath) as! MyAccountTableViewCell
//        cell.iconAccount.image = #imageLiteral(resourceName: "square.png")
//    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
