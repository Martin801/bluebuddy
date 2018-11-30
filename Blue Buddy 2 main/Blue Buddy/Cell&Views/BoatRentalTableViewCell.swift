//
//  BoatRentalTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/28/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class BoatRentalTableViewCell: UITableViewCell {

    @IBOutlet weak var imgFeatured: UIImageView!

    @IBOutlet weak var btnFlag: UIButton!
    @IBOutlet weak var imgFlag: UIImageView!
    @IBOutlet weak var btnChat: UIButton!
    @IBOutlet weak var lblPrice: UILabel!
    @IBOutlet weak var lblPostedBy: UILabel!
    @IBOutlet weak var imgProduct: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgBg: UIImageView!
    @IBOutlet weak var lblAddress: UILabel!
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
    
    @IBAction func onClickBtnProfile(_ sender: UIButton) {
    }
    @IBAction func onClickBtnChat(_ sender: UIButton) {
        if ((onClickOtherBtn) != nil) {
            onClickOtherBtn!()
        }
    }
}
