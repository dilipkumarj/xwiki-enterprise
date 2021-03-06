/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.test.po.blog;

import org.xwiki.test.po.platform.ViewPage;

/**
 * Represents the blog home page.
 * 
 * @version $Id$
 * @since 3.2M3
 */
public class BlogHomePage extends ViewPage
{
    /**
     * The create blog post form.
     */
    private final CreateBlogPostPane createBlogPostPane = new CreateBlogPostPane();

    /**
     * Opens the blog home page.
     * 
     * @return the blog home page
     */
    public static BlogHomePage gotoPage()
    {
        getUtil().gotoPage("Blog", "WebHome");
        return new BlogHomePage();
    }

    /**
     * @return the create blog post form
     */
    public CreateBlogPostPane getCreateBlogPostPane()
    {
        return createBlogPostPane;
    }
}
