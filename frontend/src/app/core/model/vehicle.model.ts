export interface Vehicle {
  id: number;
  model: string;
  color: 'BRANCA' | 'PRATA' | 'PRETA';
  manufacturingYear: number;
  basePrice: number;
  pcd: boolean;
  pessoaJuridica: boolean;

}
