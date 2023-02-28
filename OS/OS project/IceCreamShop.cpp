#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <iostream>
#include <ctime>

using namespace std;

string flavor[4]={"chocolate","vanilla","mango","strawberry"};
double priceFlavor[4]={300,250,350,250};
double priceTopping[3]={50,60,70};
string topping[3]={"sprinkles","chocolate chips","caramel syrup"};

int ticket=40;
bool gotIceCream[40];
bool gotTopping[40];
int flavorScope[4]={11,12,7,9};
int toppingSize[3]={20,13,34};
double revenue=0.0;


sem_t ticketCounter, flavorCounter, flavor1, flavor2, flavor3, flavor4, toppingCounter, topping1, topping2, topping3, paymentCounter;
time_t start[40];
time_t ending[40];

void *iceCream(void *id);

int main()
{
	int num_of_customer;
	cout<<"\n\n-------------------------Welcome to the IceCream Shop---------------------------";
	cout<<"\n\nEnter Number of Customers [1-"<<ticket<<"]: ";
	cin>>num_of_customer;
	
	if(num_of_customer>ticket || num_of_customer <=0)
	{
		cout<<"\n\nInvalid Input!\n";
		return 0;
	}
	
	int customer_id[num_of_customer];
	
	for(int i=0; i<num_of_customer; i++)
	{
		customer_id[i]=i+1;
	}
	
	sem_init(&ticketCounter,0,1);
	sem_init(&flavorCounter,0,4);
	sem_init(&flavor1,0,1);
	sem_init(&flavor2,0,1);
	sem_init(&flavor3,0,1);
	sem_init(&flavor4,0,1);
	sem_init(&toppingCounter,0,3);
	sem_init(&topping1,0,1);
	sem_init(&topping2,0,1);
	sem_init(&topping3,0,1);
	sem_init(&paymentCounter,0,1);
	
	pthread_t customers[num_of_customer];
	for(int i=0; i<num_of_customer;i++)
	{
		pthread_create(&customers[i], 0, &iceCream, (void*) &customer_id[i]);
	}
	
	for(int i=0; i<num_of_customer;i++)
	{
		pthread_join(customers[i],NULL);
	}
	
	cout<<"\n\n-----------------------IceCream Shop- End of Day Sale--------------------------\n";
	cout<<"\n\tNumber of Customers: "<<num_of_customer;
	cout<<"\n\tRevenue Generated: "<<revenue;
	cout<<"\n\tTickets Remaining: "<<ticket<<"\n";	cout<<"----------------------------------------------------------------------------\n";

	return 0;
}

void *iceCream(void *id)
{
	int ID=*(int*)id;
	double bill=0.0;
	
	sem_wait(&ticketCounter);
	
	if(ticket<=0)
	{
		cout<<"\nCustomer No. "<<ID<<"]: Ticket Finished! Leaving Shop";
		//sleep(1);
		pthread_exit(NULL);
	}
	time(&start[ID]);
	ticket--;
	cout<<"\nCustomer No. "<<ID<<" got ticket\n";
	
	sem_post(&ticketCounter);
	
	sleep(2);
	sem_wait(&flavorCounter);
	if(flavorScope[0]<=0 && flavorScope[1]<=0 && flavorScope[2]<=0 && flavorScope[3]<=0)
	{
		cout<<"\nCustomer No. "<<ID<<" leaving Shop cause flavor finished\n";
		sleep(1);
		pthread_exit(NULL);
	}
	else
	{
		char option='n';
		
		cout<<"\nCustomer No. "<<ID<<" goes to flavor counter\n";
		
		if(!(gotIceCream[ID])){
			sem_wait(&flavor1);
			if(flavorScope[0]>0)
			{
				flavorScope[0]--;
				bill=bill+priceFlavor[0];
				cout<<"\nCustomer No. "<<ID<<" got "<<flavor[0]<<" flavored icecream\n";
				gotIceCream[ID]=true;
				//sleep(1);
			}
			sem_post(&flavor1);
		}
		
		if(!(gotIceCream[ID])){
			sem_wait(&flavor2);
			if(flavorScope[1]>0)
			{
				flavorScope[1]--;
				bill=bill+priceFlavor[1];
				cout<<"\nCustomer No. "<<ID<<" got "<<flavor[1]<<" flavored icecream\n";
				gotIceCream[ID]=true;
				sleep(1);
			}
			sem_post(&flavor2);
		}
		
		if(!(gotIceCream[ID])){
			sem_wait(&flavor3);
			if(flavorScope[2]>0)
			{
				flavorScope[2]--;
				bill=bill+priceFlavor[2];
				cout<<"\nCustomer No. "<<ID<<" got "<<flavor[2]<<" flavored icecream\n";
				gotIceCream[ID]=true;
				//sleep(1);
			}
			sem_post(&flavor3);
		}
		
		if(!(gotIceCream[ID])){
			sem_wait(&flavor4);
			if(flavorScope[3]>0)
			{
				flavorScope[3]--;
				bill=bill+priceFlavor[3];
				cout<<"\nCustomer No. "<<ID<<" got "<<flavor[3]<<" flavored icecream\n";
				gotIceCream[ID]=true;
				sleep(1);
			}
			sem_post(&flavor4);
		}

	}
	
	cout<<"\nCustomer No. "<<ID<<" leaving the flavor counter\n";
	sem_post(&flavorCounter);
	
	cout<<"\nCustomer No. "<<ID<<" going topping counter\n";
	sem_wait(&toppingCounter);
	if(toppingSize[0]<=0 && toppingSize[1]<=0 && toppingSize[2]<=0)
	{
		cout<<"\nCustomer No. "<<ID<<": No toppings available! Go to payment counter\n";
		sleep(1);
		pthread_exit(NULL);
	}
	else
	{	
		if(!(gotTopping[ID])){
			sem_wait(&topping1);
			if(toppingSize[0]>0)
			{
				toppingSize[0]--;
				cout<<"\nCustomer No. "<<ID<<" got "<<topping[0]<<" topping\n";
				gotTopping[ID]=true;
				bill=bill+priceTopping[0];
			}
			sem_post(&topping1);
		}
		
		if(!(gotTopping[ID])){
			sem_wait(&topping2);
			if(toppingSize[1]>0)
			{
				toppingSize[1]--;
				cout<<"\nCustomer No. "<<ID<<" got "<<topping[1]<<" topping\n";
				gotTopping[ID]=true;
				bill=bill+priceTopping[1];
			}
			sem_post(&topping2);
		}
		
		if(!(gotTopping[ID])){
			sem_wait(&topping3);
			if(toppingSize[2]>0)
			{
				toppingSize[2]--;
				cout<<"\nCustomer No. "<<ID<<" got "<<topping[2]<<" topping\n";
				gotTopping[ID]=true;
				bill=bill+priceTopping[2];
			}
			sem_post(&topping3);
		}
	}
	cout<<"\nCustomer No. "<<ID<<" leaving topping counter\n";
	sem_post(&toppingCounter);
	
	cout<<"\nCustomer No. "<<ID<<" going payment counter\n";
	
	sem_wait(&paymentCounter);
	revenue=revenue+bill;
	cout<<"\nCustomer No. "<<ID<<" Paid: Rs."<<bill<<"\n";
	time(&ending[ID]);
	cout<<"\nCustomer No. "<<ID<<" spent "<<difftime(ending[ID],start[ID])<<" seconds in the shop\n";
	sem_post(&paymentCounter);
	
	cout<<"\nCustomer No. "<<ID<<" leaving IceCream Shop\n";
	
	return NULL;
}


