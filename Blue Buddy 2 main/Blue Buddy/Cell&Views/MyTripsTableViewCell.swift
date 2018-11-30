//
//  MyTripsTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/20/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MyTripsTableViewCell: UITableViewCell {
    
    @IBOutlet weak var btnInviteBuddy: UIButton!
    @IBOutlet weak var lblInvite: UILabel!
    @IBOutlet weak var llbcnstrnt2: NSLayoutConstraint!
    @IBOutlet weak var lblDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblNo: UILabel!
    @IBOutlet weak var imgBg: UIImageView!
    @IBOutlet weak var btnEdit: UIButton!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblName: UILabel!
    var onClickDetails: (() -> Void)? = nil
    var onClickEdit: (() -> Void)? = nil
    var onClickInvite: (() -> Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func onClickInviteBuddy(_ sender: UIButton) {
        if ((onClickInvite) != nil) {
            onClickInvite!()
        }
    }
    @IBAction func onClickBtnDetails(_ sender: UIButton) {
        if ((onClickDetails) != nil) {
            onClickDetails!()
        }
    }
    @IBAction func onClickBtnEdit(_ sender: UIButton) {
        if ((onClickEdit) != nil) {
            onClickEdit!()
        }
    }
}
