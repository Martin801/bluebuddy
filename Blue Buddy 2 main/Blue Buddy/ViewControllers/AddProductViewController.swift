//
//  AddProductViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/4/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel

class AddProductViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UITextViewDelegate, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate {

    @IBOutlet weak var btnHideDetails: UIButton!
    @IBOutlet weak var viewUpdate: UIView!
    @IBOutlet weak var viewOtherHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewOther: UIView!
    @IBOutlet weak var txtOther: UITextField!
    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var contentVHghtConstraint: NSLayoutConstraint!
    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var txtPrice: UITextField!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var txtViewDesc: UITextView!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var lblCategory: UILabel!
    @IBOutlet weak var txtProductName: UITextField!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    
    @IBOutlet weak var btnSelectCat: UIButton!
    let arrNames = ["Scuba Diving", "Freediving", "Spearfishing", "Fishing", "Boats", "Others"]
    var lengthyLabel: MarqueeLabel?
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var productData = ProductDetailsBO()
    var strProduct = String()
    var arrImages = [UIImage]()
    var indexVal = Int()
    var hideDetails = Bool()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if productData.is_hidden {
            hideDetails = true
            btnHideDetails.setImage(UIImage(named: "tick1.png"), for: .normal)
        }
        else {
            hideDetails = false
            btnHideDetails.setImage(UIImage(named: "tick2.png"), for: .normal)
        }
        
        let myColor = UIColor.lightGray
        tblViewList.layer.borderColor = myColor.cgColor
        tblViewList.layer.borderWidth = 1.0
        txtViewDesc.layer.borderColor = myColor.cgColor
        txtViewDesc.layer.borderWidth = 1.0
        btnSelectCat.contentHorizontalAlignment = .right
        btnNext.layer.cornerRadius = 5.0
        contentVHghtConstraint.constant = btnNext.frame.origin.y + 120
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        txtDescHgtConstraint.constant = 85
        txtPrice.leftViewMode = UITextFieldViewMode.always
        let lblFullPrice = UILabel(frame: CGRect(x: 0, y: 0, width: 30, height: txtPrice.frame.size.height))
        lblFullPrice.text = "$  "
        lblFullPrice.textColor = .darkGray
        lblFullPrice.font = txtPrice.font
        txtPrice.leftView = lblFullPrice
        viewOther.isHidden = true
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        if productData.product_id.isEmpty
        {
            viewUpdate.isHidden = true
            btnNext.isHidden = false
        }
        else{
            viewUpdate.isHidden = false
            btnNext.isHidden = true
            txtPrice.text = productData.price
            txtProductName.text          = productData.name
            txtViewDesc.textColor           = .black
            txtViewDesc.text                = productData.product_desc
            txtLocation.leftView        = lengthyLabel
            txtLocation.leftViewMode    = .always
            lengthyLabel?.text          = productData.user_loc
            strAddress                  = productData.user_loc
            strProduct = productData.product_type
            let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtViewDesc.frame.width, font: txtViewDesc.font!, string: productData.product_desc) + 25
            let val = valHgt < 85 ? 85 : valHgt
            txtDescHgtConstraint.constant  = val
            contentVHghtConstraint.constant = btnNext.frame.maxY + 60 + val
            viewUpdate.isHidden = false
            btnNext.isHidden = true
            lblCategory.textColor = .black
            if productData.product_type.contains("Others:")
            {
                let arrval = productData.product_type.components(separatedBy: ":")
                txtOther.text = arrval.last
                lblCategory.text = arrval.first
                viewOtherHgtConstraint.constant = 40
                viewOther.isHidden = false
            }
            else
            {
                lblCategory.text = productData.product_type.capitalized
            }
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
    
    
    // MARK: -
    // MARK: - UIButton Action Methods

    @IBAction func onClickBtnSub(_ sender: UIButton) {
        
        viewOtherHgtConstraint.constant = 0
        viewOther.isHidden = true
        lblCategory.text = "Select product type"
        lblCategory.textColor = .lightGray
        strProduct = ""

    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnCategory(_ sender: UIButton) {
        UIView.animate(withDuration: 0.4, animations: {
            let hghtcons = 150
            self.tblViewHgtConstraint.constant = CGFloat(hghtcons)
            self.contentVHghtConstraint.constant = self.btnNext.frame.maxY + 210
        }, completion: { (completed) in
            
        })
    }

    @IBAction func onClickBtnNext(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if (txtProductName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your product name", title: "BLUE BUDDY", viewController: self)
        }
        else if strProduct.isEmpty {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else if strProduct == "Others: " && (txtOther.text?.isEmpty)!
        {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (txtViewDesc.text == "Enter product description")
        {
            Common.showAlert(message: "Please enter your product description", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtPrice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your product price", title: "BLUE BUDDY", viewController: self)
        }
        
        else{
            let latitude: Double = Double((placeCoord?.latitude)!)
            let longitude: Double = Double((placeCoord?.longitude)!)
            let dictVal = NSMutableDictionary()
            dictVal["pr_category"]          = strProduct == "Others: " ? "Others: " + txtOther.text! : strProduct
            dictVal["pr_name"]              = txtProductName.text
            dictVal["pr_description"]       = txtViewDesc.text
            dictVal["pr_location"]          = strAddress
            dictVal["pr_price"]             = txtPrice.text
            dictVal["pr_lat"]               = String(latitude)
            dictVal["pr_long"]               = String(longitude)
            dictVal["pr_hide_details"]  = hideDetails == true ? "1":"0"
            
            for i in 0..<arrImages.count
            {
                if i == 0
                {
                    dictVal["pr_img1"]               = arrImages[i]
                }
                else if i == 1
                {
                    dictVal["pr_img2"]               = arrImages[i]
                }
                else if i == 2
                {
                    dictVal["pr_img3"]               = arrImages[i]
                }
                else if i == 3
                {
                    dictVal["pr_img4"]               = arrImages[i]
                }
            }
            
            MarketParser.callCreateProductWebService(dictVal, complete: { (status, strMsg, strId) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                MarketParser.callProductDetailsWebService(["id" : strId], complete: { (status, strMsg, objDetails) in
                                    if status
                                    {
                                        DispatchQueue.main.async {
                                            let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ProductDetailsViewController") as! ProductDetailsViewController
                                            let objValue = objDetails
                                            objValue.product_id = strId as String
                                            detailsVC.objProductDetails = objValue
                                            detailsVC.indexVal = self.indexVal
                                            self.navigationController?.pushViewController(detailsVC, animated: true)
                                        }
                                    }
                                })
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

      //  let listVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FeatureListViewController") as! FeatureListViewController
       // self.navigationController?.pushViewController(listVC, animated: true)

    }
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this product?", preferredStyle: UIAlertControllerStyle.alert)
        let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            let dictVal = NSMutableDictionary()
            dictVal["type"] = "product"
            dictVal["data_id"] = self.productData.product_id
            TripsParser.callDeleteService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                var isFound:Bool = false
                                if let viewControllers = self.navigationController?.viewControllers {
                                    for viewController in viewControllers {
                                        // some process
                                        if viewController.isKind(of: self.indexVal == 1 ? CourseListViewController.self : MyListingViewController.self) {
                                            isFound = true
                                            if self.indexVal == 1 {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateCourseList"), object: nil)
                                            }
                                            else if self.indexVal == 2
                                            {
                                                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateMyList"), object: nil)
                                            }
                                            else{
                                                
                                            }
                                            self.navigationController?.popToViewController(viewController, animated: true)
                                        }
                                    }
                                    if !isFound
                                    {
                                        self.navigationController?.popViewController(animated: true)
                                    }
                                }
                            }
                        })
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                }
                else{
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            })
        })
        let cancelAction = UIAlertAction(title: NSLocalizedString("NO", comment: "No action"), style: .cancel, handler: nil)
        alert.addAction(okAction)
        alert.addAction(cancelAction)
        self.present(alert, animated: true, completion: nil)
        
    }
    @IBAction func onClickBtnUpdate(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if (txtProductName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your product name", title: "BLUE BUDDY", viewController: self)
        }
        else if strProduct.isEmpty {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
        else if strProduct == "Others: " && (txtOther.text?.isEmpty)!
        {
            Common.showAlert(message: "Enter your activity type", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (txtViewDesc.text == "Enter product description")
        {
            Common.showAlert(message: "Please enter your product description", title: "BLUE BUDDY", viewController: self)
        }
        else if (strAddress.isEmpty) {
            Common.showAlert(message: "Please enter your location", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtPrice.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your product price", title: "BLUE BUDDY", viewController: self)
        }
            
        else{
            
            let dictVal = NSMutableDictionary()
            dictVal["pr_category"]          = strProduct == "Others: " ? "Others: " + txtOther.text! : strProduct
            dictVal["pr_name"]              = txtProductName.text
            dictVal["pr_description"]       = txtViewDesc.text
            dictVal["pr_location"]          = strAddress
            dictVal["pr_price"]             = txtPrice.text
            dictVal["pr_id"]                = productData.product_id
            dictVal["pr_hide_details"]  = hideDetails == true ? "1":"0"
            
            if (placeCoord != nil) {
                let latitude: Double = Double((placeCoord?.latitude)!)
                let longitude: Double = Double((placeCoord?.longitude)!)
                dictVal["pr_lat"]               = String(latitude)
                dictVal["pr_long"]               = String(longitude)
            }
            

            
            for i in 0..<arrImages.count
            {
                if i == 0
                {
                    dictVal["pr_img1"]               = arrImages[i]
                }
                else if i == 1
                {
                    dictVal["pr_img2"]               = arrImages[i]
                }
                else if i == 2
                {
                    dictVal["pr_img3"]               = arrImages[i]
                }
                else if i == 3
                {
                    dictVal["pr_img4"]               = arrImages[i]
                }
                else
                
                {
                    
                }
            }
            MarketParser.callUpdateProductWebService(dictVal, arrImages.count == 0 ? false : true,complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                            DispatchQueue.main.async {
                                MarketParser.callProductDetailsWebService(["id" : self.productData.product_id], complete: { (status, strMsg, objDetails) in
                                    if status
                                    {
                                        DispatchQueue.main.async {
                                            let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ProductDetailsViewController") as! ProductDetailsViewController
                                            let objValue = objDetails
                                            objValue.product_id = self.productData.product_id as String
                                            detailsVC.objProductDetails = objValue
                                            detailsVC.indexVal = self.indexVal
                                            self.navigationController?.pushViewController(detailsVC, animated: true)
                                        }
                                    }
                                })
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
        lblCategory.text = arrNames[indexPath.row]
        lblCategory.textColor = .black
        UIView.animate(withDuration: 0.4, animations: {
            self.contentVHghtConstraint.constant -= 150
            self.tblViewHgtConstraint.constant = 0
        }, completion: { (completed) in
            
        })
        strProduct = arrNames[indexPath.row].capitalized

        
        if arrNames[indexPath.row] == "Others" {
            strProduct = "Others: "
            viewOtherHgtConstraint.constant = 40
            self.contentVHghtConstraint.constant += 40
            viewOther.isHidden = false
        }
        else{
            viewOtherHgtConstraint.constant = 0
            viewOther.isHidden = true
        }

    }
    
    // MARK: -
    // MARK: - UITextView Delegates
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtViewDesc.text == "Enter product description"
        {
            txtViewDesc.text = ""
            txtViewDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtViewDesc.text.isEmpty
        {
            txtViewDesc.text = "Enter product description"
            txtViewDesc.textColor = .lightGray
        }
    }
    func textViewDidChange(_ textView: UITextView) {
        
        self.textViewDynamicallyIncreaseSize()
        
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        let length = (textView.text?.characters.count)! + text.characters.count - range.length
        if length < 251 {
            return true
        }
        else{
            return false
        }
    }
    
    // MARK: -
    // MARK: - Other Methods
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtViewDesc.sizeThatFits(self.txtViewDesc.bounds.size)
        let higntcons = contentSize.height < 85 ? 85 : contentSize.height
        txtDescHgtConstraint.constant = higntcons
        contentVHghtConstraint.constant = btnNext.frame.maxY + 70
        
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if txtViewDesc.text.isEmpty
        {
            txtViewDesc.text = "Provide more details about your service"
            txtViewDesc.textColor = .lightGray
        }
        self.view.endEditing(true)
    }
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        if textField == txtLocation
        {
            view.endEditing(true)
            let center = CLLocationCoordinate2D(latitude: APP_DELEGATE.currLoc.coordinate.latitude, longitude: APP_DELEGATE.currLoc.coordinate.longitude)
            let northEast = CLLocationCoordinate2D(latitude: center.latitude + 0.001,
                                                   longitude: center.longitude + 0.001)
            let southWest = CLLocationCoordinate2D(latitude: center.latitude - 0.001,
                                                   longitude: center.longitude - 0.001)
            let viewport = GMSCoordinateBounds(coordinate: northEast, coordinate: southWest)
            let config = GMSPlacePickerConfig(viewport: viewport)
            let placePicker = GMSPlacePickerViewController(config: config)
            placePicker.delegate = self
            present(placePicker, animated: true, completion: nil)
        }
    }
    
    
    // MARK: -
    // MARK: - GMSPlacePickerViewControllerDelegate Methods
    func placePicker(_ viewController: GMSPlacePickerViewController, didPick place: GMSPlace) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("Place name \(place.name)")
        print("Place address \(place.formattedAddress ?? "")")
        print("Place attributions \(String(describing: place.attributions))")
        print("Coordinates::\(place.coordinate.latitude)")
        placeCoord = place.coordinate
        txtLocation.text = " "
        txtLocation.placeholder = nil
        txtLocation.leftView = lengthyLabel
        txtLocation.leftViewMode = .always
        lengthyLabel?.text = place.formattedAddress
        if place.formattedAddress != nil {
            strAddress = place.formattedAddress!
        }
        else {
            Common.getAddressFromCurrentLatLong { (strAddresReturned) in
                self.strAddress = strAddresReturned
                self.lengthyLabel?.text = strAddresReturned
            }
        }
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }

    @IBAction func btnHideDetailsAction(_ sender: UIButton) {
        hideDetails = !hideDetails
        if hideDetails {
            sender.setImage(UIImage(named: "tick1.png"), for: .normal)
        }
        else {
            sender.setImage(UIImage(named: "tick2.png"), for: .normal)
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
