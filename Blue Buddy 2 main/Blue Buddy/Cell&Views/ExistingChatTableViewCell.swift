//
//  ExistingChatTableViewCell.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 24/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit

class ExistingChatTableViewCell: UITableViewCell {

    @IBOutlet weak var lblMsg: UILabel!
    @IBOutlet weak var lblTime: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var imgBG: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
