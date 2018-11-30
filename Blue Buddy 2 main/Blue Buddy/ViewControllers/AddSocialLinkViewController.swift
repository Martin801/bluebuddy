//
//  AddSocialLinkViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/3/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class AddSocialLinkViewController: UIViewController {
    @IBOutlet weak var cnsLblAddSocialTop: NSLayoutConstraint!
    @IBOutlet weak var txtId: UITextField!
    @IBOutlet weak var lblPopUpTitle: UILabel!
    @IBOutlet weak var lblUrl: UILabel!
    @IBOutlet weak var viewPopUp: UIView!
    @IBOutlet weak var btnFB: UIButton!
    @IBOutlet weak var btnGoogle: UIButton!
    @IBOutlet weak var btnInstagram: UIButton!
    @IBOutlet weak var btnTwitter: UIButton!
    @IBOutlet weak var txtURLSocial: UITextField!
    
    @IBOutlet weak var cnsBtmClickToCOnnect: NSLayoutConstraint!
    var selectedTag: Int?
    var strFbURL: String? = ""
    var strTwitterURL: String? = ""
    var strYouTubeURL: String? = ""
    var strInstagramURL: String? = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        viewPopUp.isHidden = true
        let myColor = UIColor.darkGray
        btnFB.layer.borderColor         = myColor.cgColor
        btnFB.layer.borderWidth         = 1.0
        btnFB.layer.cornerRadius         = 5.0
        btnGoogle.layer.borderColor     = myColor.cgColor
        btnGoogle.layer.borderWidth     = 1.0
        btnGoogle.layer.cornerRadius         = 5.0
        btnInstagram.layer.borderColor  = myColor.cgColor
        btnInstagram.layer.borderWidth  = 1.0
        btnInstagram.layer.cornerRadius         = 5.0
        btnTwitter.layer.borderColor    = myColor.cgColor
        btnTwitter.layer.borderWidth    = 1.0
        btnTwitter.layer.cornerRadius         = 5.0
        // Do any additional setup after loading the view.
        
        print("screenType:", UIDevice.current.screenType.rawValue)
        if UIDevice.current.screenType.rawValue == "iPhone 4 or iPhone 4S" {
            cnsLblAddSocialTop.constant = 0
            cnsBtmClickToCOnnect.constant = 10
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action Methods
    @IBAction func onClickBtnLogOut(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
        UserDefaults.standard.set(false, forKey: kisLogin)
        UserDefaults.standard.set(false, forKey: kisFBLogin)
        UserDefaults.standard.set("", forKey: kFBID)
        UserDefaults.standard.set("", forKey: kUserPass)
        UserDefaults.standard.set("", forKey: kUserEmail)

    }
    @IBAction func onClickBtnSocialMedia(_ sender: UIButton) {
//        sender.isSelected = !sender.isSelected
        selectedTag = sender.tag
        var urlString = "https://www.facebook.com/"
        switch sender.tag {
        case 1:
            urlString = "https://www.facebook.com/"
            lblPopUpTitle.text = "Enter your Facebook url"
        case 2:
            urlString = "https://www.twitter.com/"
            lblPopUpTitle.text = "Enter your Twitter url"
        case 3:
            urlString = "https://www.instagram.com/"
            lblPopUpTitle.text = "Enter your Instagram url"
        case 4:
            urlString = "https://www.youtube.com/"
            lblPopUpTitle.text = "Enter your YouTube url"
        default:
            break
        }
        
        txtURLSocial.text = urlString
        let attributedString = NSMutableAttributedString(string: urlString)
        
        attributedString.addAttribute(NSUnderlineStyleAttributeName, value: NSUnderlineStyle.styleSingle.rawValue, range: NSMakeRange(0, attributedString.length))
        lblUrl.attributedText = attributedString
        viewPopUp.isHidden = false
        self.view.bringSubview(toFront: viewPopUp)

    }

    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnPopUpActions(_ sender: UIButton) {
        self.view.endEditing(true)
        viewPopUp.isHidden = true
        if sender.tag == 1
        {
            
        }
        else
        {
            if selectedTag == 1
            {
                strFbURL = txtURLSocial.text! + txtId.text!
            }
            else if (selectedTag == 2)
            {
                strTwitterURL = txtURLSocial.text! + txtId.text!
            }
            else if (selectedTag == 3)
            {
                strInstagramURL = txtURLSocial.text! + txtId.text!
            }
            else
            {
                strYouTubeURL = txtURLSocial.text! + txtId.text!
            }
        }
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        let dictVal = NSMutableDictionary()
        dictVal["fb_url"]       = (strFbURL?.isEmpty)! ? "" : strFbURL
        dictVal["twtr_url"]     = (strTwitterURL?.isEmpty)! ? "" : strTwitterURL
        dictVal["ingm_url"]     = (strInstagramURL?.isEmpty)! ? "" : strInstagramURL
        dictVal["ggle_url"]     = (strYouTubeURL?.isEmpty)! ? "" : strYouTubeURL
        dictVal["next_step"]    = "6"
        UserDefaults.standard.set("6", forKey:kPageNo)
        UserParser.callUpdateProfileService(dictVal) { (status, strMsg) in
            if status
            {
                DispatchQueue.main.async {
                    let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OtherLinkViewController") as! OtherLinkViewController
                    self.navigationController?.pushViewController(aboutVC, animated: true)
                }
            }
        }
    }
    @IBAction func onTapOutSidePopUp(_ sender: UITapGestureRecognizer) {
        viewPopUp.isHidden = true
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
