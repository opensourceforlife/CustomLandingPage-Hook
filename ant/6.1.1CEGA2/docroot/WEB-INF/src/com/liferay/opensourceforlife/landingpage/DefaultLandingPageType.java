/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.StringPool;

/**
 * @author tejas.kanani
 */
public class DefaultLandingPageType extends LandingPageType {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath (javax.portlet.PortletRequest)
	 */
	@Override
	public String getLandingPagePath(final HttpServletRequest request) {

		return StringPool.BLANK;
	}

}
