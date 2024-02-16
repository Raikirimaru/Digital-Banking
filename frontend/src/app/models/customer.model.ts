export interface Customer {
    id: number;
    name: string;
    email: string;
    DateOfBirth : Date;
    tel : string;
}

export interface CustomerDTOS {
    customerDTO: Customer[];
    totalpage: number;
}