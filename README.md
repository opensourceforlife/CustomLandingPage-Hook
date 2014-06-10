![OS4L](http://4.bp.blogspot.com/-q425Jqhwvj4/UlUz5zOFOqI/AAAAAAAABCk/xWmjUJy-czg/s640/Custom+Landing+Page+Banner.png "OS4L")
Custom Landing Page Hook
========================

After a user logs into Liferay, it is a common requirement to be able to redirect them to a different page based on their Organization or Site membership. This hook allows you to specify the desired redirection behavior using a properties file setting. Based on the properties file, it will redirect the user to their private page, public page, their default site's public/private homepage, or their default organization's public/private homepage.

It's a Custom Landing Page Hook developed in Liferay 6.1.1 GA2. It will allow you to choose on which page user will land on after login.
Right now it's having below options for landing page.


Set this property as per your needs for landing page after user log in. It could be from one of the value from below options

1. userPrivatePage 
2. userPublicPage 
3. sitePublicPage 
4. sitePrivatePage
5. organizationPublicPage
6. organizationPrivatePage
7. role
8. userGroup

```
custom.landing.page.type={userPrivatePage/userPublicPage/sitePublicPage/sitePrivatePage/organizationPublicPage/organizationPrivatePage/role/userGroup} 
```

This propery need to be set in `portal.properties` located in Custom Landing Page hook itself. Either you can modify it directly in war. Or you can download the source code from Git repositry and you can build war from source with your desired changes.


#Getting Started
------------------
Here I'll be showing you how you can use this hook and leverage the flexibility for defining landing page once user logs in.

##1) Download
Before going further, make sure you know the version of your current Liferay instance which you want to use Custom Landing Page Hook for. Next, go to Custom Landing Page Hook's [Release section] (https://github.com/opensourceforlife/CustomLandingPage-Hook/releases "Release Section") in order to download the hook for your Liferay version.

_Make sure you check the supported Liferay versions for each release before downloading._


##2) Configure
Next we need to configure the hook accroding to the need. In order to do that follow below steps.
* Open downloaded CustomLandingPage-hook-*.war using winzip/winrar/7-zip/any other tool
* Go to WEB-INF/classes
* Open portal.properties
* Set the value of `custom.landing.page.type` property as per your need.  
i.e. `custom.landing.page.type=userGroup`
* Save the changes to the same war file

<br />
####Advance Configuration

**1. Site / Organization**<br />
If you want any specific page wihtin a Site/Organization be a landing page for that Site/Organization instead of it's default home page(Generally a first page), you can achieve it by creating a custom attribute for Site/Organization and providing required page's __friendlyURL__.
 
For,
* Site/Organization's Public page, create custom attribute of type *"TextField"* with key `"landingPagePublic"` and define value as page's friendlyURL. i.e. /welcome
* Site/Organization's Private page, create custom attribute of type *"TextField"* with key `"landingPagePrivate"` and define value as page's friendlyURL. i.e. /myhome
 
![Define specific landing page for site/organization via Custom Attribute in Custom Landing Page Hook](http://1.bp.blogspot.com/-wkY6NNk1PH0/UmIo3mBJhQI/AAAAAAAABDI/FndO8-ehf2E/s1600/Defining+Custom+Attribute.png "Define specific landing page for site/organization via Custom Attribute in Custom Landing Page Hook")
 
<br />
**2. Usergroup / Role** (only for Liferay 6.2+)<br />
Starting from v1.2.2, new provision got added to define custom landing page for users who is member of specific User Group & Role. So for this you just need to create custom attribute for Usergroup/Role with key __"landingPage"__. And provide desired landing page's **complete relative URL**.<br />
i.e. ```landingPage = /web/mysite/home```

<br />

##3) Deploy
Next we need to deploy the updated war. To do that, you can simple drop the war file in Liferay `deploy` folder.

<br /><br />

##Version History


####v1.2.2
* Added provision to define custom landing page for specific Role & User Group
* For role & userGroup, provide landing page complete relative URL in role OR userGroup's custom attribute with key "landingPage"   
```landingPage = /web/mysite/home```

######Supported Liferay Versions
* Liferay 6.2.0 CE +
* Liferay 6.2.10 EE +

######Download 
* Packaged War & Complete source can be download from [Release section] (https://github.com/opensourceforlife/CustomLandingPage-Hook/releases "Release Section") 

<br />
####v1.2.1
There ia a new option added by which any specific page wihtin a Site/Organization can be made a landing page for that Site/Organization instead of their home page.

Just create a custom attribute of Site/Organization by mentioning the page friendlyURL.

For defining,

* Site/Organization's Public page, create custom attribute of type *"TextField"* with key `"landingPagePublic"` and define value as page's friendlyURL. i.e. /welcome
* Site/Organization's Private page, create custom attribute of type *"TextField"* with key `"landingPagePrivate"` and define value as page's friendlyURL. i.e. /myhome


######Supported Liferay Versions
* Liferay 6.2.0 CE

######Download 
* Packaged War & Complete source can be download from [Release section] (https://github.com/opensourceforlife/CustomLandingPage-Hook/releases "Release Section")   
   
   
<br />
####v1.1
* Equivalent to [v1.2](https://www.liferay.com/marketplace/-/mp/application/17676547 "v1.2") in Liferay Marketplace submission)

######Supported Liferay Versions
* Liferay 6.1.1 CE
* Liferay 6.1.20 EE
* Liferay 6.1.2+ CE
* Liferay 6.1.30+ EE

######Download 
* Packaged War & Complete source can be download from [Release section] (https://github.com/opensourceforlife/CustomLandingPage-Hook/releases "Release Section")  

   
<br /><br />
   
## Custom Landing Page Hook on Liferay Marketplace !! 
#### !! Stands Runnder Up in [Liferay Marketplace App Contest !! ](http://discover.liferay.com/marketplace-app-contest "Liferay Marketplace App Contest")
* [Custom Landing Page Hook](http://www.liferay.com/marketplace/-/mp/application/17676547 "Custom Landing Page Hook")

<br /><br />

### For any Issue/Support
For any issue/support, you can either reach out to me at [admin@opensourceforlife.com](mailto:admin@opensoruceforlife.com "admin@opensourceforlife.com"). Or you can create a issue in [Github issues](https://github.com/opensourceforlife/CustomLandingPage-Hook/issues "Github Issues")

Have a happy Custom Landing !!! :)
