/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author Tejas Kanani
 */
public class UserPrivatePageType implements LandingPageType
{
	/*
	 * (non-Javadoc)
	 * @see com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath
	 * (javax.portlet.PortletRequest)
	 */
	public String getLandingPagePath(final HttpServletRequest request) throws PortalException,
			SystemException
	{
		return CustomLandingPageUtil.getUserDisplayURL(request, Boolean.TRUE);
	}
}
