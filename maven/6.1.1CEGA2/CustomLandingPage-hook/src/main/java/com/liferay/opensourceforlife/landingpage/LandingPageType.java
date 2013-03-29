/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author Tejas Kanani
 */
public interface LandingPageType
{

	public String getLandingPagePath(HttpServletRequest request) throws PortalException,
			SystemException;
}
