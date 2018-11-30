//
//  ServiceTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/26/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import FloatRatingView
class ServiceTableViewCell: UITableViewCell {

//    @IBOutlet weak var imgVwPhone: UIImageView!
//    @IBOutlet weak var imgVwEmail: UIImageView!
    @IBOutlet weak var imgFeatured: UIImageView!
    @IBOutlet weak var btnFlag: UIButton!
    @IBOutlet weak var imgFlag: UIImageView!
    @IBOutlet weak var imgBG: UIImageView!
    @IBOutlet weak var btnEdit: UIButton!
    @IBOutlet weak var serviceRate: FloatRatingView!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var servicePostedBy: UILabel!
    @IBOutlet weak var serviceName: UILabel!
    @IBOutlet weak var imgService: UIImageView!
    
    
    var onClickReport: (() -> Void)? = nil
    
    
    var onClickDetails: (() -> Void)? = nil
    var onClickOtherBtn: (() -> Void)? = nil

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    @IBAction func onClickBtnReport(_ sender: UIButton) {
        if ((onClickReport) != nil) {
            onClickReport!()
        }
    }
    @IBAction func onClickBtnDetails(_ sender: UIButton) {
        if ((onClickDetails) != nil) {
            onClickDetails!()
        }
    }
    @IBAction func onClickBtnEdit(_ sender: UIButton) {
        if ((onClickOtherBtn) != nil) {
            onClickOtherBtn!()
        }
    }
}
