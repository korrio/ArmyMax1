// Generated code from Butter Knife. Do not modify!
package com.ame.armymax.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class EverythingFragment$$ViewInjector {
  public static void inject(Finder finder, final com.ame.armymax.fragment.EverythingFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131623961, "field 'postStatus'");
    target.postStatus = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131624009, "field 'postPhoto'");
    target.postPhoto = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131624010, "field 'postVideo'");
    target.postVideo = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131624196, "field 'listView' and field 'scrollView'");
    target.listView = (android.widget.ListView) view;
    target.scrollView = (android.widget.ListView) view;
    view = finder.findRequiredView(source, 2131624198, "field 'footer'");
    target.footer = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131624195, "field 'mPullToRefreshView'");
    target.mPullToRefreshView = (com.yalantis.pulltorefresh.library.PullToRefreshView) view;
  }

  public static void reset(com.ame.armymax.fragment.EverythingFragment target) {
    target.postStatus = null;
    target.postPhoto = null;
    target.postVideo = null;
    target.listView = null;
    target.scrollView = null;
    target.footer = null;
    target.mPullToRefreshView = null;
  }
}
