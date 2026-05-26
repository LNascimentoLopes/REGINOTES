import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { Home } from './home/home';
import { Preview } from './preview/preview';
import { Edit } from './edit/edit';
import { Trash } from './trash/trash';
import { authGuard } from './core/guards/auth.guards';

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {
        path: 'login',
        component: Login,
    },
    {
        path: 'register',
        component: Register,
    },

    { 
        path: 'home',
        component: Home,
        canActivate: [authGuard],
    },
    { 
        path: 'preview/:id',
        component: Preview,
        canActivate: [authGuard],
    },
    { 
        path: 'edit',
         component: Edit
         , canActivate: [authGuard] },
    {
        path: 'edit/:id',
        component: Edit,
        canActivate: [authGuard],
    },
    {
        path: 'trash',
        component: Trash,
        canActivate: [authGuard],
        
    },
    {
        path: '**',
        redirectTo: 'login',
    }
];
