//
//  NotificationsTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 1/2/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit

class NotificationsTableViewCell: UITableViewCell {

    @IBOutlet weak var btnIgnoreHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblActivity: UILabel!
    @IBOutlet weak var btnIgnore: UIButton!
    @IBOutlet weak var btnAcceptHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnAccept: UIButton!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var imgUser: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgBg: UIImageView!
    var onClickSelectedIndex: ((Bool) -> Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @IBAction func onClickBtnIgnore(_ sender: UIButton) {
        if ((onClickSelectedIndex) != nil) {
            onClickSelectedIndex!(false)
        }
    }

    @IBAction func onClickBtnAccept(_ sender: UIButton) {
        if ((onClickSelectedIndex) != nil) {
            onClickSelectedIndex!(true)
        }
    }
}
