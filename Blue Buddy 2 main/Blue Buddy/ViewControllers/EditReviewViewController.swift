//
//  EditReviewViewController.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 11/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit
import FloatRatingView
class EditReviewViewController: UIViewController, FloatRatingViewDelegate{
    

    @IBOutlet weak var txtDescHgtConstarint: NSLayoutConstraint!
    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var txtDesc: UITextView!
    @IBOutlet weak var txtTitle: UITextField!
    @IBOutlet weak var rateReview: FloatRatingView!
    var onClickUpdate: ((ReviewBO) -> Void)? = nil
    var objReview = ReviewBO()
    override func viewDidLoad() {
        super.viewDidLoad()
        let myColor = UIColor.darkGray
        txtDesc.layer.cornerRadius = 3.0
        txtDesc.layer.borderWidth = 1.0
        txtDesc.layer.borderColor = myColor.cgColor
        txtDesc.text = objReview.reviewDesc
        txtTitle.text = objReview.review_title
        rateReview.delegate = self
        rateReview.rating = Float(objReview.rating)!

        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtDesc.frame.width, font: txtDesc.font!, string: objReview.reviewDesc) + 25
        txtDescHgtConstarint.constant = valHgt
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
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnUpdate(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if rateReview.rating == 0
        {
            Common.showAlert(message: "Please give rating", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtTitle.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review title", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (txtDesc.text == "Description") || (txtDesc.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review description", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["rev_title"] = txtTitle.text
            dictVal["rev_description"] = txtDesc.text
            dictVal["rev_rating"] = String(rateReview.rating)
            dictVal["rev_for"] = objReview.review_for
            dictVal["for_id"] = objReview.for_id
            dictVal["id"] = objReview.review_id
            MarketParser.callUpdateReviewService(dictVal, complete: { (status, strMsg) in
                if status{
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                let valueRev = self.objReview
                                valueRev.reviewDesc = self.txtDesc.text
                                valueRev.review_title = self.txtTitle.text!
                                valueRev.rating = String(self.rateReview.rating)
                                if ((self.onClickUpdate) != nil)
                                {
                                    self.onClickUpdate!(valueRev)
                                }
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

    }

    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
    }
    
    /**
     Returns the rating value when touch events end
     */
    func floatRatingView(_ ratingView: FloatRatingView, didUpdate rating: Float) {
        
    }

    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtDesc.text == "Description"
        {
            txtDesc.text = ""
            txtDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Description"
            txtDesc.textColor = .lightGray
            
        }
    }
    
    func textViewDidChange(_ textView: UITextView) {
        
        self.textViewDynamicallyIncreaseSize()
        
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        let length = (textView.text?.characters.count)! + text.characters.count - range.length
        if length < 150 {
            return true
        }
        else{
            return false
        }
    }
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtDesc.sizeThatFits(self.txtDesc.bounds.size)
        let higntcons = contentSize.height < 55 ? 55 :contentSize.height
        txtDescHgtConstarint.constant = higntcons

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
