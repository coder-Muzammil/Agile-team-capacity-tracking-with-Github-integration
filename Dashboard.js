import React, { useState, useEffect } from 'react';
import { Box, Grid, Paper, Typography } from '@mui/material';
import Sidebar from './Sidebar';
import GraphA from './GraphA';
import GraphB from './GraphB';
import GraphC from './GraphC';
import GraphD from './GraphD';

const Dashboard = () => {
  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>DASHBOARD</Typography>
      
      <Grid container spacing={3}>
        {/* Left Sidebar */}
        <Grid item xs={12} md={3}>
          <Sidebar />
        </Grid>
        
        {/* Main Content */}
        <Grid item xs={12} md={9}>
          <Grid container spacing={3}>
            {/* Region Data */}
            <Grid item xs={12}>
              <Paper sx={{ padding: 2 }}>
                <Grid container spacing={2}>
                  {['North America 19:55', 'Africa 4198.52', 'South America 15:54', 'Asia 16.3', 'Europe 27.6', 'Panama 2.21'].map((item) => (
                    <Grid item xs={6} sm={4} key={item}>
                      <Typography variant="body1">{item}</Typography>
                    </Grid>
                  ))}
                </Grid>
              </Paper>
            </Grid>
            
            {/* Main Graphs */}
            <Grid item xs={12} md={6}>
              <GraphA />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <GraphB />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <GraphC />
            </Grid>
            
            <Grid item xs={12} md={6}>
              <GraphD />
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;