//
//  MyReviewsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 1/2/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit

class MyReviewsViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    @IBOutlet weak var tblMyReviews: UITableView!
    @IBOutlet weak var lblNoData: UILabel!
    var prototypeCell = ReviewTableViewCell()
    var arrReviews = NSMutableArray()
    override func viewDidLoad() {
        super.viewDidLoad()
        tblMyReviews.isHidden = true
        lblNoData.layer.cornerRadius = lblNoData.frame.size.height/2
        lblNoData.clipsToBounds = true
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblNoData.text = strCounter
                    }
                }
            }
 }
        UserParser.callMyReviewService([ : ]) { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    if arrList.count != 0
                    {
                        self.arrReviews = arrList as! NSMutableArray
                        self.tblMyReviews.isHidden = false
                        self.lblNoData.isHidden = true
                        self.tblMyReviews.reloadData()
                    }
                    else
                    {
                        self.tblMyReviews.isHidden = true
                        self.lblNoData.isHidden = false

                    }
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.lblNoData.isHidden = false
                    self.tblMyReviews.isHidden = true
//                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        }

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

    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrReviews.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "review_Cell", for: indexPath) as! ReviewTableViewCell
        let objVal = arrReviews[indexPath.row] as! ReviewBO
        cell.imgBg.layer.borderWidth = 0.5
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.lblDate.text = objVal.date
        cell.lblName.text = objVal.itemName.capitalized
        cell.lblTitle.text = objVal.review_title
        cell.lblreview.text = objVal.reviewDesc
        cell.viewRating.rating = Float(objVal.rating)!
        cell.lblDescHgtConstraint.constant = objVal.cellHgt
        cell.imgUser.isHidden = true
        cell.btnProfile.setImage(#imageLiteral(resourceName: "edit1.png"), for: .normal)
        cell.imgUser.isHidden = true
        cell.onClickProfile = {() -> Void in
            DispatchQueue.main.async {
                let reviewVC:EditReviewViewController = self.storyboard!.instantiateViewController(withIdentifier: "EditReviewViewController") as! EditReviewViewController
                reviewVC.objReview = objVal
                reviewVC.onClickUpdate = {(objRevVal) -> Void in
                    
                    self.arrReviews.remove(objVal)
                    self.arrReviews.add(objRevVal)
                    self.tblMyReviews.reloadData()
                }
                self.navigationController?.pushViewController(reviewVC, animated: true)
            }
        }
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        self.prototypeCell = tblMyReviews.dequeueReusableCell(withIdentifier: "review_Cell")! as! ReviewTableViewCell
        
        let objVal = arrReviews[indexPath.row] as! ReviewBO
        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblreview.frame.size.width, font: prototypeCell.lblreview.font, string: objVal.reviewDesc)
        objVal.cellHgt = cellHgt + 25
        return 198 + cellHgt - 25
        //        return 198
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let objVal = arrReviews[indexPath.row] as! ReviewBO
        if objVal.review_for == "service"
        {
            MarketParser.callServiceDetailsWebService(["id" : objVal.item_id], complete: { (status, strMsg, objServiceDetails) in
                if status
                {
                    DispatchQueue.main.async {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
                        detailsVC.objServiceDetails = objServiceDetails
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                }
                else{
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            })
        }
        else if objVal.review_for == "product"
        {
            MarketParser.callProductDetailsWebService(["id" : objVal.item_id], complete: { (status, strMsg, objDetails) in
                if status
                {
                    DispatchQueue.main.async {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ProductDetailsViewController") as! ProductDetailsViewController
                        let objValue = objDetails
                        objValue.product_id = objVal.item_id as String
                        detailsVC.objProductDetails = objValue
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                }
                else{
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            })

        }
        else if objVal.review_for == "charter"
        {
            MarketParser.callChaterDetailsService(["id" : objVal.item_id], complete: { (status, strMsg, objDetails) in
                if status
                {
                    DispatchQueue.main.async {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "RentalDetailsViewController") as! RentalDetailsViewController
                        detailsVC.objCharterDetails = objDetails
                        detailsVC.indexVal = 0
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                }
                else{
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            })
        }
        else if objVal.review_for == "course"
        {
            MarketParser.callCourseDetailsService(["id" : objVal.item_id], complete: { (status, strMsg, objCourseDetails) in
                if status
                {
                    DispatchQueue.main.async {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CourseDetailsViewController") as! CourseDetailsViewController
                        let objCourse = objCourseDetails
                        objCourse.course_id = objVal.item_id
                        detailsVC.objDetails = objCourse
                        self.navigationController?.pushViewController(detailsVC, animated: true)
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
