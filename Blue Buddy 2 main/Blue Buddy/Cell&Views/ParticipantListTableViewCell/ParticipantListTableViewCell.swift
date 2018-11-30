//
//  ParticipantListTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/12/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class ParticipantListTableViewCell: UITableViewCell {

    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var imgBg: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
