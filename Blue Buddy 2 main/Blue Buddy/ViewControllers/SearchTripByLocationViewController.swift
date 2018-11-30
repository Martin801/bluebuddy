//
//  SearchTripByLocationViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/28/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel
class SearchTripByLocationViewController: UIViewController, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate{

    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var lblSliderValue: UILabel!
    @IBOutlet weak var lblMinVal: UILabel!
    @IBOutlet weak var lblMaxVal: UILabel!
    @IBOutlet weak var slider: UISlider!
    @IBOutlet weak var viewSelectLoc: UIView!
    @IBOutlet weak var viewCurrentLoc: UIView!
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var lengthyLabel: MarqueeLabel?
    var isCurrentLoc = Bool()
    
    var onClickSearch: ((NSArray) -> Void)? = nil
    override func viewDidLoad() {
        super.viewDidLoad()
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        if isCurrentLoc {
            viewCurrentLoc.isHidden = false
            viewSelectLoc.isHidden = true
//            self.callTripSearch("22.5726", "88.3639", "5")
        }
        else
        {
            viewSelectLoc.isHidden = false
            viewCurrentLoc.isHidden = true
            
        }
        
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action
    @IBAction func onClickBtnSearchLocation(_ sender: UIButton) {
        var latitude: Double = 22.5726
        var longitude: Double = 88.3639
        if isCurrentLoc
        {
            self.callTripSearch(String(latitude) as NSString, String(longitude) as NSString, "50")
        }
        else{
            latitude = Double((placeCoord?.latitude)!)
            longitude = Double((placeCoord?.longitude)!)
            self.callTripSearch(String(latitude) as NSString, String(longitude) as NSString, lblSliderValue.text! as NSString)
            
        }
    }

    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onChangeSliderValue(_ sender: UISlider) {
        let value = Int (sender.value)
        lblSliderValue.text = String (value) + " km"
    }
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        let center = CLLocationCoordinate2D(latitude: -33.865143, longitude: 151.2099)
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
    
    func callTripSearch(_ strLat: NSString, _ strLong: NSString, _ strRadius: NSString)
    {
        let dictVal = NSMutableDictionary()
        dictVal["radius"]               = strRadius
        dictVal["location_lat"]         = strLat
        dictVal["location_long"]        = strLong
        TripsParser.callSerachByLocTripsService(dictVal) { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    if ((self.onClickSearch) != nil) {
                        self.onClickSearch!(arrList)
                    }
                    self.navigationController?.popViewController(animated: true)
                }
                
            }
            else
            {
                DispatchQueue.main.async {
                    Common.showAlert(message: "No data found", title: "BLUE BUDDY", viewController: self)
                }
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
