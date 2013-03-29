/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Tejas Kanani
 */
public class DefaultLandingPageType implements LandingPageType
{

	public String getLandingPagePath(final HttpServletRequest request)
	{

		return StringPool.BLANK;
	}

}
