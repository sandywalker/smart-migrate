package org.smart.migrate.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * Guide Controller, Use To Controll Step UI
 * 向导控制器，用于控制上一步下一步界面显示
 * @author Sandy Duan
 *
 */
public class GuideController {

	private JPanel navigation; //step navigation(步数导航)
	
	private JPanel steps; //all steps(步数主面板)

	private static final String PANEL_PREFIX = "panelGuide";

	private static final String STEP_PREFIX = "card";
        
        private boolean enabled = true;
	
	
	public GuideController(JPanel navigation,JPanel steps){
		this.navigation = navigation;
		this.steps = steps;
	}
	
        
        public void setEnabled(boolean enabled){
            this.enabled = enabled;
        }

	

	public JPanel getNavigation() {
		return navigation;
	}

	public void setNavigation(JPanel navigation) {
		this.navigation = navigation;
	}
	
	/**
         * Init Nav Events
	 * 初始化导航事件
	 */
	public void initEvents(){
		for(Component component:navigation.getComponents()){
			if (component instanceof JPanel){
				((JPanel)component).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
                                            if (enabled){
						setStepByName(e.getComponent().getName());
                                            }    
					}
				});
			}
		}
	}
	
	/**
         * Get all step count
	 * 获取总步数
	 * @return
	 */
	public int getStepCount(){
		return navigation.getComponentCount();
	}
	
	/**
         * Get current step index
	 * 获取当前的步数
	 * @return
	 */
	public int getIndex(){
		
		for(Component component:navigation.getComponents()){
			if (component.getBackground().equals(Color.WHITE)){
				return Integer.parseInt(component.getName().replaceAll("[A-Za-z]+",""))-1;
			}
		}
		return -1;
	}
	
	/**
         * Get step index by component name
	 * 根据控件名获取当前的步数
	 * @param name
	 * @return
	 */
	public int getIndexByName(String name){
		for(Component component:navigation.getComponents()){
				if (component.getName().equals(name)){
					return Integer.parseInt(component.getName().replaceAll("[A-Za-z]+",""))-1;
				}
		}
		return -1;
	}
	
	/**
         * Get nav panel by component name;
	 * 根据控件名获取导航面板
	 * @param name
	 * @return
	 */
	public JPanel getNavPanelByName(String name){
		for(Component component:navigation.getComponents()){
			if (component.getName().equals(name)&&component instanceof JPanel){
				return (JPanel)component;
			}
		}
		return null;
	}
	/**
         * Get step panel by component name
	 * 根据步数面板名称获取步数面板
	 * @param name
	 * @return
	 */
	public JPanel getStepPanelByName(String name){
		for(Component component:steps.getComponents()){
			if (component.getName().equals(name)&&component instanceof JPanel){
				return (JPanel)component;
			}
		}
		return null;
	}
	
	/**
         * Get nav panel by current index
	 * 根据当前步数获取导航面板
	 * @param index
	 * @return
	 */
	public JPanel getNavPanelByIndex(int index){
		String name = PANEL_PREFIX+(index+1);
		return getNavPanelByName(name);
	}
	
	/**
         * Disable all navigation controls
	 * 所有的导航面板变成未激活状态
	 */
	public void disableAll(){
		Color defColor = UIManager.getColor("Panel.background");
		for(Component component:navigation.getComponents()){
			((JPanel)component).setBorder(new LineBorder(defColor));
			component.setBackground(defColor);
		}
		for(Component component:steps.getComponents()){
			if (component.isVisible()){
				component.setVisible(false);
			}
		}
		steps.validate();
	}
	
	/**
         * Active step by idnex
	 * 将指定的步数设置为激活状态
	 * @param index
	 */
	public void active(int index){
		JPanel panel = getNavPanelByIndex(index);
		if (panel!=null){
			panel.setBackground(Color.WHITE);
			panel.setBorder(new LineBorder(new Color(51,153,255)));
			String stepName = STEP_PREFIX+(index+1);
			((CardLayout)steps.getLayout()).show(steps,stepName);
		}
	}
	
	/**
         * Active step  by name of nav panels
	 * 根据导航面板名称激活
	 * @param name
	 */
	public void activeByName(String name){
		JPanel panel = getNavPanelByName(name);
		if (panel!=null){
			panel.setBackground(Color.WHITE);
			((CardLayout)steps.getLayout()).show(steps, name.replace(PANEL_PREFIX, STEP_PREFIX));
		}
	}
	
	/**
         * Next step
	 * 下一步
	 */
	public void next(){
		
		int current = getIndex();
		int index = 0;
		if (current>=0&&current<getStepCount()-1){
			index = current+1; 
		}
		disableAll();
		active(index);
	}
	
	/**
         * Previous step
	 * 上一步
	 */
	public void prev(){
		
		int current = getIndex();
		int index = getStepCount()-1;
		if (current>0){
			index = current-1;
		}
		disableAll();
		active(index);
		
	}
	
	/**
         * Go to step
	 * 指定某个步骤
	 * @param step
	 */
	public void setStep(int step){
		disableAll();
		active(step);
	}
	
	/**
         * Go to step by name
	 * 根据控件名指定步骤
	 * @param name
	 */
	public void setStepByName(String name){
		int index = getIndexByName(name);
		setStep(index);
	}
	
	
	
	
	/**
         * Is first step
	 * 是否是第一步
	 * @return
	 */
	public boolean isFirst(){
		return getIndex()==0;
	}
	/**
         * Is last step
	 * 是否是最后一步
	 * @return
	 */
	public boolean isLast(){
		return getIndex()==getStepCount()-1;
	}
}
