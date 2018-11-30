//
//  TripsDetailsBO.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/7/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class TripsDetailsBO: NSObject {
    var activity: String            = ""
    var user_pic: String            = ""
    var user_email: String          = ""
    var user_id: String             = ""
    var user_name: String           = ""
    var trip_desc: String           = ""
    var event_date: String          = ""
    var event_date1: String          = ""
    var event_id: String            = ""
    var is_editable: Bool           = false
    var event_loc: String           = ""
    var no_of_participant: String   = ""
    var is_participated: Bool       = false
    var is_flagged: Bool            = false
    var cellHgt: CGFloat            = 0.0
    var firebase_id: String = ""
}
