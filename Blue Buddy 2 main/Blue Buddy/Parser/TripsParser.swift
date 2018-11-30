//
//  TripsParser.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/7/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class TripsParser: NSObject {

    // MARK: -
    // MARK: - All Trips Service
    class func callAllTripsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/alltrip/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                
                TripsParser.parseTripList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    
    class func parseTripList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrTripList: NSMutableArray?
        arrTripList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "details") as! NSArray

            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let tripData = TripsDetailsBO()
                let activity                = dictDetails.value(forKey: "activity") as! String
                tripData.activity           = activity.capitalized
                tripData.user_pic           = dictDetails.value(forKey: "creator_dp") as! String
                tripData.user_email         = dictDetails.value(forKey: "creator_email") as! String
                let fname = dictDetails.value(forKey: "creator_fname") as! String
                let lname = dictDetails.value(forKey: "creator_lname") as! String
                tripData.user_name          = fname.capitalized + " " + lname.capitalized
                tripData.event_date         = dictDetails.value(forKey: "event_date") as! String
                tripData.event_date1        = dictDetails.value(forKey: "original_event_date") as! String
                tripData.event_id           = dictDetails.value(forKey: "event_id")as! String
                let editVal                 = dictDetails.value(forKey: "is_editable")as! String
                tripData.is_editable        = editVal == "1" ? true : false
                tripData.event_loc          = dictDetails.value(forKey: "location")as! String
                tripData.no_of_participant  = dictDetails.value(forKey: "no_of_participant")as! String
                let partVal                 = dictDetails.value(forKey: "participated")as! String
                tripData.is_participated    = partVal == "1" ? true : false
                tripData.user_id            = dictDetails.value(forKey: "user_id")as! String
                let flagged                 = dictDetails.value(forKey: "is_flagged")as! String
                tripData.is_flagged         = flagged == "1" ? true : false
                tripData.trip_desc          = dictDetails.value(forKey: "description") as! String
                tripData.firebase_id        = dictDetails.value(forKey: "creator_firebase_reg_id") as! String
                arrTripList?.add(tripData)
            }
            
        }
        completeBlock(status!, "", arrTripList!)
        
    }


    // MARK: -
    // MARK: - Trips Details Service
    class func callTripsDetailsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray, _ objDetails: TripsDetailsBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/details") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                TripsParser.parseTripParticipantList(dictResult, complete: { (status, strMsg, arrList, tripData) in
                    completeBlock(status, strMsg, arrList, tripData)
                })
            }
                
            else{
                let val = TripsDetailsBO()
                completeBlock(isSuccess, "Something Went Wrong", [], val)
            }
        }
    }
    class func parseTripParticipantList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray, _ objTrip: TripsDetailsBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrPartList: NSMutableArray?
        let tripData = TripsDetailsBO()
        arrPartList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "participation_list") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let userData = ProfileBO()
                userData.email              = dictDetails.value(forKey: "Email") as! String
                userData.profile_pic        = dictDetails.value(forKey: "Profile_pic") as! String
                userData.first_name         = dictDetails.value(forKey: "First_name") as! String
                userData.last_name          = dictDetails.value(forKey: "Last_name") as! String
                userData.location           = dictDetails.value(forKey: "Location")as! String
                userData.lat                = dictDetails.value(forKey: "Location_lat")as! String
                userData.userId             = dictDetails.value(forKey: "Id")as! String
                userData.long               = dictDetails.value(forKey: "Location_long")as! String
                userData.phone              = dictDetails.value(forKey: "Phone") as! String
                userData.firebase_id        = dictDetails.value(forKey: "Firebase_reg_id") as! String
                arrPartList?.add(userData)
            }
            let dictDetails : NSDictionary = dictResult.value(forKey: "details") as! NSDictionary
            
            let activity                = dictDetails.value(forKey: "activity") as! String
            tripData.activity           = activity.capitalized
            tripData.user_pic           = dictDetails.value(forKey: "creator_dp") as! String
            let fname = dictDetails.value(forKey: "creator_fname") as! String
            let lname = dictDetails.value(forKey: "creator_lname") as! String
            tripData.user_name          = fname.capitalized + " " + lname.capitalized
            tripData.event_date         = dictDetails.value(forKey: "event_date") as! String
            tripData.event_id           = dictDetails.value(forKey: "event_id")as! String
            let editVal                 = dictDetails.value(forKey: "is_editable")as! String
            tripData.is_editable        = editVal == "1" ? true : false
            tripData.event_loc          = dictDetails.value(forKey: "location")as! String
            tripData.no_of_participant  = dictDetails.value(forKey: "participants")as! String
            let partVal                 = dictDetails.value(forKey: "participated")as! String
            tripData.is_participated    = partVal == "1" ? true : false
            tripData.user_id            = dictDetails.value(forKey: "user_id")as! String
            tripData.trip_desc          = dictDetails.value(forKey: "description") as! String

            
        }
        completeBlock(status!, "", arrPartList!, tripData)
        
    }


    // MARK: -
    // MARK: - Create Trips Service
    class func callCreateTripsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objTripDetails: TripsDetailsBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                TripsParser.parseTripDetails(dictResult, complete: { (status, strMsg, objTripData) in
                    completeBlock(status, strMsg, objTripData)
                })
            }
                
            else{
                let val = TripsDetailsBO()
                completeBlock(isSuccess, "Something Went Wrong", val)
            }
        }
    }

    
    class func parseTripDetails(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objData: TripsDetailsBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let strMsg = dictResult.value(forKey: "message") as? String
        let dictDetails = dictResult.value(forKey: "trip_details") as? NSDictionary
        let tripData = TripsDetailsBO()

        if status!{
            tripData.activity = dictDetails?.value(forKey: "activity") as! String
            tripData.user_pic = dictDetails?.value(forKey: "creator_dp") as! String
            tripData.user_email = dictDetails?.value(forKey: "creator_email") as! String
            let fname = dictDetails?.value(forKey: "creator_fname") as! String
            let lname = dictDetails?.value(forKey: "creator_lname") as! String
            tripData.user_name = fname + lname
            tripData.firebase_id = dictDetails?.value(forKey: "creator_firebase_reg_id") as! String
            tripData.event_date = dictDetails?.value(forKey: "event_date") as! String
            tripData.event_id = dictDetails?.value(forKey: "event_id")as! String
            let editVal = dictDetails?.value(forKey: "is_editable")as! String
            tripData.is_editable = editVal == "1" ? true : false
            tripData.event_loc = dictDetails?.value(forKey: "location")as! String
            tripData.no_of_participant = dictDetails?.value(forKey: "no_of_participant")as! String
            let partVal = dictDetails?.value(forKey: "participated")as! String
            tripData.is_participated = partVal == "1" ? true : false
            tripData.user_id = dictDetails?.value(forKey: "user_id")as! String
        }

        completeBlock(status!, strMsg!, tripData)

    }
    // MARK: -
    // MARK: - Participate in Trips Service
    class func callParticipateInTripsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/participate/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    // MARK: -
    // MARK: - Delete participation in Trips Service
    class func callDeleteParticipationInTripsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/participatedelete") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    // MARK: -
    // MARK: - Update Trips Service
    class func callUpdateTripService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    
    
    // MARK: -
    // MARK: - Invite Buddy Trips Service
    class func callInviteBuddyService(_ dictPram: NSDictionary, _ isFromBuddy: Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        var url = NSString()
        if isFromBuddy
        {
            url = "Trip/invite"
        }
        else{
            url = "profile/invitebuddy"
        }
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: url) as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }

    // MARK: -
    // MARK: - Search Trips by Location Service
    class func callSerachByLocTripsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList :NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/search/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                TripsParser.parseTripList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    // MARK: -
    // MARK: - Search User Service
    class func callSearchUserService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Search") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                TripsParser.parseSearchUserData(dictResult, complete: { (status, strmsg, arrList) in
                    completeBlock(status, strmsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    class func parseSearchUserData(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrList :NSArray = dictResult.value(forKey: "user_details_arr") as! NSArray
        let arrSearchList: NSMutableArray?
        arrSearchList = []
        if status!{
            for index in 0 ..< arrList.count
            {
                let dictValue : NSDictionary = arrList.object(at: index) as! NSDictionary
                let profileData = ProfileBO()
                profileData.first_name = dictValue.value(forKey: "First_name") as! String
                profileData.last_name = dictValue.value(forKey: "Last_name") as! String
                profileData.about = dictValue.value(forKey: "About") as! String
                profileData.email = dictValue.value(forKey: "Email") as! String
                profileData.userId = dictValue.value(forKey: "Id") as! String
                profileData.interested_in = dictValue.value(forKey: "Interested_in") as! String
                profileData.location = dictValue.value(forKey: "Location") as! String
                profileData.lat = dictValue.value(forKey: "Location_lat") as! String
                profileData.long = dictValue.value(forKey: "Location_long") as! String
                profileData.phone = dictValue.value(forKey: "Phone") as! String
                profileData.profile_pic = dictValue.value(forKey: "Profile_pic") as! String
                profileData.social = dictValue.value(forKey: "Social") as! String
                profileData.social_id = dictValue.value(forKey: "Social_id") as! String
                profileData.website_link = dictValue.value(forKey: "Website_link") as! String
                profileData.fb_url = dictValue.value(forKey: "fb_url") as! String
                profileData.ggle_url = dictValue.value(forKey: "ggle_url") as! String
                profileData.ingm_url = dictValue.value(forKey: "ingm_url") as! String
                profileData.twtr_url = dictValue.value(forKey: "twtr_url") as! String
                
                let buddy = dictValue.value(forKey: "isBuddy") as! String
                profileData.isBuddy = buddy
                
                profileData.firebase_id = dictValue.value(forKey: "Firebase_reg_id") as! String
                let arrCert :NSArray = dictValue.value(forKey: "certification_details") as! NSArray
                for index in 0 ..< arrCert.count
                {
                    let dictVal : NSDictionary = arrCert.object(at: index) as! NSDictionary
                    let cert = dictVal.value(forKey: "Cert_type") as! String
                    profileData.arrCertificate.add(cert)
                    
                }
                arrSearchList?.add(profileData)
            }
        }
        completeBlock(status!, "", arrSearchList!)
    }
    
    // MARK: -
    // MARK: - Accept Participate in Trips Service
    class func callAcceptParticipateInTripsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Trip/participateaccept") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    
    

    // MARK: -
    // MARK: -
    // MARK: - Common Section
    
    // MARK: - Delete  Service
    class func callDeleteService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Common/delete") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    // MARK: - Chat  Service
    class func callChatService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeaderAndLoader(serverUrl: APP_SERVICE(str: "Common/chat") as! String, postValues: dictPram, methodName: "POST", isLoader: false) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    // MARK: - Notification Count  Service
    class func callNotificationCountService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strCounter: String) -> Void) {
        BaseParser.callWebServiceWithHeaderForGet(serverUrl: APP_SERVICE(str: "Common/countnotification") as! String, postValues: dictPram, methodName: "GET", isLoader:false) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strCounter = dictResult.value(forKey: "counter") as? String
                completeBlock(status!, strCounter!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }

    //Common/countnotification
    // MARK: - Report  Service
    class func callReportService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Flag/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    
    // MARK: - Buddy List  Service
    class func callBuddyListService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Buddy/list") as! String, postValues: [ : ], methodName: "GET") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }
    
    
}
