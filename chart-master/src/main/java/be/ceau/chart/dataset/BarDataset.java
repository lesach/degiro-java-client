/*
	Copyright 2020 Marceau Dewilde <m@ceau.be>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.chart.dataset;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import be.ceau.chart.enums.BorderSkipped;
import be.ceau.chart.objects.OptionalArray;

@JsonInclude(Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class BarDataset extends BackgroundBorderHoverDataset<BarDataset, BigDecimal> {

	/**
	 * @see #setLabel(String)
	 */
	private String label;

	/**
	 * @see #setXAxisID(String)
	 */
	private String xAxisID;

	/**
	 * @see #setYAxisID(String)
	 */
	private String yAxisID;

	/**
	 * @see #setBorderSkipped(List)
	 */
	private final List<BorderSkipped> borderSkipped = new OptionalArray<>();

	/**
	 * @see #setLabel(String)
	 */
	public String getLabel() {
	    return this.label;
	}

	/**
	 * The label for the dataset which appears in the legend and tooltips
	 */
	public BarDataset setLabel(String label) {
	    this.label = label;
		return this;
	}

	/**
	 * @see #setXAxisID(String)
	 */
	public String getXAxisID() {
	    return this.xAxisID;
	}

	/**
	 * The ID of the x axis to plot this dataset on
	 */
	public BarDataset setXAxisID(String xAxisID) {
	    this.xAxisID = xAxisID;
		return this;
	}

	/**
	 * @see #setYAxisID(String)
	 */
	public String getYAxisID() {
	    return this.yAxisID;
	}

	/**
	 * The ID of the y axis to plot this dataset on
	 */
	public BarDataset setYAxisID(String yAxisID) {
	    this.yAxisID = yAxisID;
		return this;
	}

	/**
	 * @see #setBorderSkipped(List)
	 */
	public List<BorderSkipped> getBorderSkipped() {
	    return this.borderSkipped;
	}

	/**
	 * @see #setBorderSkipped(List)
	 */
	public BarDataset addBorderSkipped(BorderSkipped borderSkipped) {
	    this.borderSkipped.add(borderSkipped);
		return this;
	}

	/**
	 * Which edge to skip drawing the border for. Options are 'bottom', 'left', 'top', and 'right'
	 */
	public BarDataset setBorderSkipped(List<BorderSkipped> borderSkipped) {
	    this.borderSkipped.clear();
	    if (borderSkipped != null) {
	    	this.borderSkipped.addAll(borderSkipped);
	    }
		return this;
	}

	/**
	 * Sets the backing data list to the argument, replacing any data already
	 * added or set
	 * 
	 * @param p
	 *            The data to plot in a line
	 */
	public BarDataset setData(int... p) {
		clearData();
		if (p != null) {
			for (int datum : p) {
				this.data.add(new BigDecimal(datum));
			}
		}
		return this;
	}

	/**
	 * Sets the backing data list to the argument, replacing any data already
	 * added or set
	 * 
	 * @param p
	 *            The data to plot in a line
	 */
	public BarDataset setData(double... p) {
		clearData();
		if (p != null) {
			for (double v : p) {
				this.data.add(new BigDecimal(String.valueOf(v)));
			}
		}
		return this;
	}

	/**
	 * Add the data point to this {@code Dataset}
	 * 
	 * @see #setData(Collection)
	 */
	public BarDataset addData(int p) {
		this.data.add(new BigDecimal(p));
		return this;
	}

	/**
	 * Add the data point to this {@code Dataset}
	 *
	 * @see #setData(Collection)
	 */
	public BarDataset addData(double p) {
		this.data.add(new BigDecimal(String.valueOf(p)));
		return this;
	}

}
