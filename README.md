![OS4L](http://2.bp.blogspot.com/-2SirYYg_HHE/UT6o-wro58I/AAAAAAAAAzI/OwNf6nkXUMU/s1600/upload_00033073.png "OS4L")
Custom Landing Page Hook
========================

After a user logs into Liferay, it is a common requirement to be able to redirect them to a different page based on their Organization or Site membership. This hook allows you to specify the desired redirection behavior using a properties file setting. Based on the properties file, it will redirect the user to their private page, public page, their default site's public/private homepage, or their default organization's public/private homepage.

It's a Custom Landing Page Hook developed in Liferay 6.1.1 GA2. It will allow you to choose on which page user will land on after login.
Right now it's having below options for landing page.


Set this property as per your needs for landing page after user log in. It could be from one of the value from below options
1. userPrivatePage, 2. userPublicPage, 3. sitePublicPage, 4. sitePrivatePage, 5. organizationPublicPage, 6. organizationPrivatePage

custom.landing.page.type={userPrivatePage/userPublicPage/sitePublicPage/sitePrivatePage/organizationPublicPage/organizationPrivatePage} 

This propery need to be set in portal.properties located in Custom Landing Page hook itself. Either you can modify it directly in war. Or you can download the source code from Git repositry and you can build war from source with your desired changes.



###### Update (New options added !!!)
There ia a new option added by which any specific page wihtin a Site/Organization can be made a landing page for that Site/Organization instead of their home page.

Just create a custom attribute of Site/Organization by mentioning the the page friendlyURL.

For defining,

* Site/Organization's Public page, create custom attribute of type "TextField" with key "landingPagePublic" and define value as page's friendlyURL. i.e. /welcome
* Site/Organization's Private page, create custom attribute of type "TextField" with key "landingPagePrivate" and define value as page's friendlyURL. i.e. /myhome

######Custom Landing Page Hook - Git Source

* https://github.com/opensourceforlife/CustomLandingPage-Hook

Maven based source code is available in Git.


######Packaged War
* [CustomLandingPage-hook-6.1.1.1.war](http://goo.gl/aPfSh "CustomLandingPage-hook-6.1.1.1.war")


## Custom Landing Page Hook on Liferay Marketplace !!
* [Custom Landing Page Hook](http://www.liferay.com/marketplace/-/mp/application/17676547 "Custom Landing Page Hook")


Have a happy Custom Landing !!! :)
