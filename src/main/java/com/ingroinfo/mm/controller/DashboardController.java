package com.ingroinfo.mm.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	@ModelAttribute
	private void UserDetailsService(Model model, Principal principal) {
		model.addAttribute("getLoggedUser", principal.getName());
	}

	@GetMapping
	public String dashboard() {
		return "/dashboard";
	}
	
	@GetMapping("/task-management")
	public String taskManagement() {
		return "/pages/dashboard/task_management_dashboard";
	}
	@GetMapping("/asset-management")
	public String assetManagement() {
		return "/pages/dashboard/asset_management_dashboard";
	}
	
	@GetMapping("/stock-management")
	public String stockManagement() {
		return "/pages/dashboard/stock_management_dashboard";
	}
	@GetMapping("/workorder-management")
	public String workorderManagement() {
		return "/pages/dashboard/workorder_management_dashboard";
	}
	
	@GetMapping("/employee-management")
	public String employeeManagement() {
		return "/pages/dashboard/employee_management_dashboard";
	}
	
	@GetMapping("/billing-management")
	public String billingManagement() {
		return "/pages/dashboard/billing_management_dashboard";
	}
	
	@GetMapping("/pump-management")
	public String pumpManagement() {
		return "/pages/dashboard/pump_management_dashboard";
	}
	
	@GetMapping("/pipe-management")
	public String pipeManagement() {
		return "/pages/dashboard/pipe_management_dashboard";
	}
	
	@GetMapping("/vehicle-management")
	public String vehicleManagement() {
		return "/pages/dashboard/vehicle_management_dashboard";
	}
	
	@GetMapping("/leakage-management")
	public String leakageManagement() {
		return "/pages/dashboard/leakage_management_dashboard";
	}
	
	@GetMapping("/meter-management")
	public String meterManagement() {
		return "/pages/dashboard/meter_management_dashboard";
	}
	
	@GetMapping("/handpump-management")
	public String handpumpManagement() {
		return "/pages/dashboard/handpump_management_dashboard";
	}
	
	@GetMapping("/borewell-management")
	public String borewellManagement() {
		return "/pages/dashboard/borewell_management_dashboard";
	}
	
	@GetMapping("/glsroht-management")
	public String glsrohtManagement() {
		return "/pages/dashboard/glsroht_management_dashboard";
	}
	
	@GetMapping("/levelcontrol-management")
	public String levelcontrolManagement() {
		return "/pages/dashboard/levelcontrol_management_dashboard";
	}
	
	@GetMapping("/waterstorage-management")
	public String waterstorageManagement() {
		return "/pages/dashboard/waterstorage_management_dashboard";
	}
	
	@GetMapping("/contact-management")
	public String contactManagement() {
		return "/pages/dashboard/contact_management_dashboard";
	}
	
}
