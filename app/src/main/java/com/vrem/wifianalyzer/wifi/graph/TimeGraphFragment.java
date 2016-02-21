/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.wifi.graph;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

public class TimeGraphFragment extends Fragment {
    private final MainContext mainContext = MainContext.INSTANCE;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_graph_content, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.timeGraphRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        ViewSwitcher viewSwitcher = (ViewSwitcher) view.findViewById(R.id.timeGraphSwitcher);

        Resources resources = getResources();
        makeGraphView(view, resources, R.id.timeGraph2, WiFiBand.TWO);
        makeGraphView(view, resources, R.id.timeGraph5, WiFiBand.FIVE);

        return view;
    }

    private void makeGraphView(View view, Resources resources, int graphViewId, WiFiBand wiFiBand) {
        AxisLabel axisLabel = new AxisLabel(0, Integer.MAX_VALUE);
        axisLabel.setEvenOnly(true);

        GraphView graphView = new GraphViewBuilder(view, graphViewId)
                .setLabelFormatter(axisLabel)
                .setVerticalTitle(resources.getString(R.string.graph_axis_y))
                .setHorizontalTitle(resources.getString(R.string.graph_time_axis_x))
                .setScrollable(true)
                .build();

        new TimeGraphAdapter(graphView, wiFiBand);
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        mainContext.getScanner().update();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}