//
//  MyAccountTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/9/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MyAccountTableViewCell: UITableViewCell {
    @IBOutlet weak var lblLine: UILabel!

    @IBOutlet weak var iconAccount: UIImageView!
    @IBOutlet weak var lblNames: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
