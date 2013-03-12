Custom Landing Page Hook
========================

After a user logs into Liferay, it is a common requirement to be able to redirect them to a different page based on their Organization or Site membership. This hook allows you to specify the desired redirection behavior using a properties file setting. Based on the properties file, it will redirect the user to their private page, public page, their default site's public/private homepage, or their default organization's public/private homepage.

It's a Custom Landing Page Hook developed in Liferay 6.1.1 GA2. It will allow you to choose on which page user will land on after login.
Right now it's having below options for landing page.


Set this property as per your needs for landing page after user log in. It could be from one of the value from below options
1. userPrivatePage, 2. userPublicPage, 3. sitePublicPage, 4. sitePrivatePage, 5. organizationPublicPage, 6. organizationPrivatePage

custom.landing.page.type={userPrivatePage/userPublicPage/sitePublicPage/sitePrivatePage/organizationPublicPage/organizationPrivatePage} 

This propery need to be set in portal.properties located in Custom Landing Page hook itself. Either you can modify it directly in war. Or you can download the source code from Git repositry and you can build war from source with your desired changes.

Custonm Landing Page Hook - Git Source
---------------------------------------
https://github.com/OS4L/CustomLandingPage-Hook

Both Ant & Maven based source code is available in Git.

Packaged War - http://goo.gl/aPfSh


Have a happy Custom Landing !!! :)
