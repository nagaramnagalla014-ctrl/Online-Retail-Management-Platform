import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent implements OnInit {
  stats: any = {};
  recentOrders: any[] = [];
  loading = true;

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.adminService.getDashboard().subscribe(res => {
      if (res.success) {
        this.stats = res.data;
        this.recentOrders = res.data.allOrders || [];
      }
      this.loading = false;
    });
  }
}
