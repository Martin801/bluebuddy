//
//  ProfileBO.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/6/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class ProfileBO: NSObject {
    var first_name: String = ""
    var last_name: String = ""
    var fullname: String = ""
    var about: String = ""
    var email: String = ""
    var userId: String = ""
    var interested_in: String = ""
    var location: String = ""
    var lat: String = ""
    var long: String = ""
    var phone: String = ""
    var profile_pic: String = ""
    var buddy_counter: String = ""
    var social: String = ""
    var social_id: String = ""
    var firebase_id: String = ""
    var website_link: String = ""
    var isBuddy: String = ""
    var fb_url: String = ""
    var ggle_url: String = ""
    var ingm_url: String = ""
    var twtr_url: String = ""
    var arrTrips: NSMutableArray = []
    var arrCertificate: NSMutableArray = []
    var arrSocialLink: NSMutableArray = []
    var arrParticipation: NSMutableArray = []

}

class NotificationBO: NSObject
{
    var activity_type: String = ""
    var first_name: String = ""
    var last_name: String = ""
    var for_id: String = ""
    var notification_id = ""
    var is_seen: String = ""
    var message: String = ""
    var notify_for: String = ""
    var profile_pic: String = ""
    var receiver_id: String = ""
    var requestor_id: String = ""
    var user_id: String = ""
    var create_date: String = ""
    var is_accepted: String = ""
    var viewed_at: String = ""
}

class MyListingBO: NSObject
{
    var section_Name: String = ""
    var type: Int = 0
    var arrValue: NSMutableArray = []
}

class CertificateBO: NSObject
{
    var Cert_level: String = ""
    var Cert_level2: String = ""
    var Cert_level3: String = ""
    var Cert_id: String = ""
    var Cert_name: String = ""
    var Cert_no: String = ""
    var Cert_type: String = ""
    var arrLevel: NSMutableArray = []
}

class FriendBO: NSObject {
    var name: String = ""
    var fid: String = ""
    var email: String = ""
    var image: String = ""
    var isOnline: String = ""
    var location: String = ""
    var lastMsg = MessageBO()
}
class MessageBO: NSObject{
    var owner: String = ""
    var message: String = ""
    var timestamp: Int = 0
    var isRead: Bool = false
    var image = UIImage()
    private var toID: String = ""
    private var fromID: String = ""
}


