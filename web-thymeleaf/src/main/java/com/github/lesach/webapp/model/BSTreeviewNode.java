package com.github.lesach.webapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BSTreeviewNode {
    // Class to match JS library: https://github.com/chniter/bstreeview
    // The text value displayed for a given tree node.
    String text;

    // The icon displayed on a given node.
    String icon;

    // A custom href attribute value for a given node.
    String href;

    // A class name or space separated list of class names to add to a given node.
    @JsonProperty("class")
    String cssClass;

    // ID attribute value to assign to a given node.
    String id;

    // Json or string array of nodes.
    String data;

    // Expand icon class name, default is fa fa-angle-down fa-fw.
    String expandIcon;

    // Collapse icon class name, default is fa fa-angle-right fa-fw.
    String collapseIcon;

    // Custom indent between node levels (rem), default is 1.25.
    BigDecimal indent;

    // margin-left value of parent nodes, default is 1.25rem.
    String parentsMarginLeft;

    //
    Boolean openNodeLinkOnNewTab;

    // Child nodes
    List<BSTreeviewNode> nodes = new ArrayList<>();
}
