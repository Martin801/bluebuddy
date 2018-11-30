//
//  SerachTripsTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/27/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class SerachTripsTableViewCell: UITableViewCell {

    @IBOutlet weak var lablInviteBuddy: UILabel!
    @IBOutlet weak var btnFlag: UIButton!
    @IBOutlet weak var imgFlag: UIImageView!
    @IBOutlet weak var imgBg: UIImageView!
    @IBOutlet weak var btnEdit: UIButton!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblPosterName: UILabel!
    @IBOutlet weak var imgUser: UIImageView!
    @IBOutlet weak var lblDesHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnAdd: UIButton!
    var onClickDetails: (() -> Void)? = nil
    var onClickReport: (() -> Void)? = nil
    var onClickParticipate: (() -> Void)? = nil
    var onClickAddBuddy: (() -> Void)? = nil
    var onClickProfile: (() -> Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    @IBAction func onClickAddBuddy(_ sender: UIButton) {
        if ((onClickAddBuddy) != nil) {
            onClickAddBuddy!()
        }
    }
    @IBAction func onClickBtnDetails(_ sender: UIButton) {
        if ((onClickDetails) != nil) {
            onClickDetails!()
        }
        
    }
    @IBAction func onClickBtnInterested(_ sender: UIButton) {
        if ((onClickParticipate) != nil) {
            onClickParticipate!()
        }
    }
    @IBAction func onClickBtnReport(_ sender: UIButton) {
        if ((onClickReport) != nil) {
            onClickReport!()
        }
    }
    @IBAction func onClickBtnProfile(_ sender: UIButton) {
        if ((onClickProfile) != nil) {
            onClickProfile!()
        }
    }

}
